# distributed_game_server

-----

## 分布式Java游戏服务器

### 所用框架
- Vert.x       构建服务间消息发送系统（这个目前可以用ZeroMQ方便的替换，为了再看看它的能力，留着吧）
- Zookeeper       分布式发现与注册（现在用Infinispan实现了，略有小问题，但还是不想换回zookeeper）
- Springboot       采用非web方式启动，注解编程，方便嫁接Spring系列和其它第三方框架
- Kryo，fastjson       此两个做为序列化工具，最终还是采用protobuf做为序列化工具了（又测了几个，发现还是proto吧）
- protobuf3       协议
- hutool       方便的工具包，顺便学学怎么写各类工具代码-----用了几天，这玩意几乎完全没有用武之地，实属废材。（已经确认，完全没用的东西）
- netty       tcp连接，毕竟Vert.x不熟，暂时先使用netty做为tcp服务器包，后期会继续变更

#### 打算围绕Vert.x打造一款开发便利的分布式游戏服务系统

##### 另外比较头疼的是缓存层怎么搭建的问题，目前有的经验是redis+mysql，方式是读取mysql中的列表保存到redis，每次从redis反序化这大列表再拿来操作，感觉嘛，效率比较低，有大神提供其它思路的，特别欢迎指点一二，如果是分布式开房间游戏，卡牌棋牌等，这些都没问题，但如果是分布式mmo，对战这些，本地缓存就是必不可少了，还不清楚这块怎么弄


### 数据库初步打算使用ignite ------ ignite败了，太复杂，分布式map目前还是比较难用的，集群化，ignite和hazelcast分别都失败了，连接几小时后会断连造成cpu负载高，查这种问题的能力还是比较欠缺，看来和分布式网格没有缘分了。经历换了zookeepr，vertx,zeromq等方式后，集群连接目前采用的是 vertx+zookeeper方案。数据库再做打算吧

##### 数据库备选 redis mysql postgresql mongodb 这几个目前是比较看好的，看看怎么组合一下(目前已初步上了MongoDB，正在学习中)
##### 缓存备选 ehcache redis（缓存的事没这么简单，再想想再想想）


