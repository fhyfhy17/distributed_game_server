# distributed_game_server

-----

## 分布式Java游戏服务器

### 所用框架
- `Vert.x`      构建服务间消息发送系统（这个目前可以用ZeroMQ方便的替换，为了再看看它的能力，留着吧）
- `Zookeeper`    分布式发现与注册
- `Springboot`       采用非web方式启动，注解编程，方便嫁接Spring系列和其它第三方框架
- `Kryo,fastjson`   此两个做为序列化工具，最终还是采用protobuf做为序列化工具了（又测了几个，发现还是proto吧）
- `protobuf3`       协议
- `hutool`       方便的工具包，顺便学学怎么写各类工具代码
- `netty`       tcp连接，毕竟Vert.x不熟，暂时先使用netty做为tcp服务器包，后期会继续变更
- `disruptor`     已经搭好，但发现它适合执行时间快的，有流水线结构的模型，目前用在项目中做dispatch的话，并没有作用，留着备用
- `MongoDB`      数据库。之后要学习怎么集群
- `ignite`     分布式Map，数据网格。现在用它实现了缓存系统和发现注册系统，之后想观察下它的分布式计算功能（分布式Map功能暂时不想用，太难配置好）



#### 打算围绕Vert.x打造一款开发便利的分布式游戏服务系统


### 数据库初步打算使用ignite ------ 经历换了zookeepr，vertx,zeromq等方式后，集群连接目前采用的是 vertx+zookeeper方案。(已更换为Infinispan + vertx，上线肯定要用Zookeeper，Zookeeper还是稳，并且嵌入式的连接，节点增多为N时，每个节点都会维护N-1个心跳)（由于底层不打算用mysql，所以就只使用ignite做连接和缓存层，数据用MongoDB，由于用Spring data ，也可以任意替换的。）


### 研究了一个多月的缓存，终于用舒服的方式实现了。  本版实现为 ignite  write-behind模式，get可以load数据进入缓存，put会调用write方法实现写数据库。  实现了只对缓存层进行操作，基本数据，有缓存的部分，无需调用数据库操作。 其它需要调用数据库的如分页、排行、特定查询等，可以调用dao， 实现了灵活的操作与配置。 如需优化：如 根据名字等条件查询数据也做缓存---这个以后再考虑。
### 缓存的备选：   ehcache，这个也有write-behind和其它灵活的配置方式，但不知道为什么对它巨长无比的类名、builder模式和非常复杂的配置不太感冒，以后可以再继续研究； caffeine 这个缓存也不错，待研究，本阶段还没有需要驱逐策略的缓存实现，以后也许有用。双层可以 redis，ehcache等。


之后的计划：
- 0 最重要的公共缓存还没有设计，工会，好友，组队这几个，要保证一致性还有很长的路要走。
- 1 把vertx再看详细点，暂时还未发现它有不可替代的作用，发消息层面，用netty和zeromq更顺手。看一下 vertx conf，做配置中心
- 2 任务系统： 做游戏，这种任务系统比较重要，想设计一个大一统的任务系统。包括：任务、成就、活动、签到等等，只要是要完成一个事件，事件里要分几个步骤，每个步骤要收集多少东西，怎么完成怎么奖励，类似于这种东西，都可以集成在这个大系统里。想把这个当成一种插件形式的，只提供，步骤、天数、开始结束时间、个数、触发条件、完成条件等，不涉及具体设计，以便灵活使用。最好是配置的。
- 3 quartz服务时间调度服务器，这个优先级比较低。
- 4 线程池研究，对现在的线程发配方式比较不满意，想研究深入一点，线程池。
- 5 监控系统研究、系统测试研究。使服务可观察，可测量。这方面知识比较少。
- 6 Jenkins、TeamCity等流程化部署
- 7 最后就是全部docker化，k8s研究



PS  了解到了 分布式conf原来只能提供键值对。也就是更适合做全局的控制配置，比如log级别，或者是取代profile，在多环境开发下，方便的控制各环境的变量
对异构的服务器，支持还是得放在properties里。excel这类的配置，可以放在统一服务器，做轮询处理或者是更改后统一通知其它端去取。
所以要使用Apollo，和自己写个conf服搞这个事

最近暂停一下了。分布式缓存这里并没有什么新的想法。
最近学习一下，
1 如何用Scala抹平Java里的设计模式
2 学习《设计数据密集型应用》这本书，了解数据在程序中的真正价值

ignite启动慢，过于复杂。 vertx的关键特性（异步）用不上。ignite的全局缓存也够呛能学会了。
下一步计划（临时，谁知道会不会改呢）：
重弄个程序：
1 用hazelcast做发现，用hazelcast的 write-behind。尽量封装得里层些，对外部也只是简单缓存，抛弃分布
式缓存的想法吧，不适合用在游戏里。
2 用zeromq做节点通信。真是又快又稳呐，或者直接用hazelcast的？
3 改成rpc，抛弃发送消息的模式，做单发，和来回的就行，里面要包含超时和异常的处理。
4 可以试试做成Kotlin版的，这样就不用Quasar了，可以用协程，也试试新东西。
