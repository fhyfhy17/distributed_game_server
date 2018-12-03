# distributed_game_server

-----

## 分布式Java游戏服务器

### 所用框架
- <font color=gray size=5>Vert.x </font>      构建服务间消息发送系统（这个目前可以用ZeroMQ方便的替换，为了再看看它的能力，留着吧）
- <font color=gray size=5>Zookeeper</font>    分布式发现与注册（现在用Infinispan实现了，略有小问题，但还是不想换回zookeeper）
- <font color=gray size=5>Springboot</font>       采用非web方式启动，注解编程，方便嫁接Spring系列和其它第三方框架
- <font color=gray size=5>Kryo，fastjson</font>       此两个做为序列化工具，最终还是采用protobuf做为序列化工具了（又测了几个，发现还是proto吧）
- <font color=gray size=5>protobuf3</font>       协议
- <font color=gray size=5>hutool</font>       方便的工具包，顺便学学怎么写各类工具代码
- <font color=gray size=5>netty</font>       tcp连接，毕竟Vert.x不熟，暂时先使用netty做为tcp服务器包，后期会继续变更

#### 打算围绕Vert.x打造一款开发便利的分布式游戏服务系统

##### 另外比较头疼的是缓存层怎么搭建的问题，目前有的经验是redis+mysql，方式是读取mysql中的列表保存到redis，每次从redis反序化这大列表再拿来操作，感觉嘛，效率比较低，有大神提供其它思路的，特别欢迎指点一二，如果是分布式开房间游戏，卡牌棋牌等，这些都没问题，但如果是分布式mmo，对战这些，本地缓存就是必不可少了，还不清楚这块怎么弄


### 数据库初步打算使用ignite ------ ignite败了，太复杂，分布式map目前还是比较难用的，集群化，ignite和hazelcast分别都失败了，连接几小时后会断连造成cpu负载高，查这种问题的能力还是比较欠缺，看来和分布式网格没有缘分了。经历换了zookeepr，vertx,zeromq等方式后，集群连接目前采用的是 vertx+zookeeper方案。数据库再做打算吧

##### 数据库备选 redis mysql postgresql mongodb 这几个目前是比较看好的，看看怎么组合一下(目前已初步上了MongoDB，正在学习中)
##### 缓存备选 ehcache redis（缓存的事没这么简单，再想想再想想）


### 研究了一个多月的缓存，终于用舒服的方式实现了。  本版实现为 ignite  write-behind模式，get可以load数据进入缓存，put会调用write方法实现写数据库。  实现了只对缓存层进行操作，基本数据，有缓存的部分，无需调用数据库操作。 其它需要调用数据库的如分页、排行、特定查询等，可以调用dao， 实现了灵活的操作与配置。 如需优化：如 根据名字等条件查询数据也做缓存---这个以后再考虑。
### 缓存的备选：   encache，这个也有write-behind和其它灵活的配置方式，但不知道为什么对它巨长无比的类名、builder模式和非常复杂的配置不太感冒，以后可以再继续研究； caffeine 这个缓存也不错，待研究，本阶段还没有需要驱逐策略的缓存实现，以后也许有用。


