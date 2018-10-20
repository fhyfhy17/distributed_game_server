# distributed_game_server

-----

## 分布式Java游戏服务器

### 所用框架
- Vert.x       构建服务间消息发送系统
- Zookeeper       分布式发现与注册
- Springboot       采用非web方式启动，注解编程，方便嫁接Spring系列和其它第三方框架
- Kryo，fastjson       Vert.x传递消息为String，此两个做为序列化工具，最终还是采用protobuf做为序列化工具了
- protobuf3       协议
- hutool       方便的工具包，顺便学学怎么写各类工具代码-----用了几天，这玩意几乎完全没有用武之地，实属废材。
- netty       tcp连接，毕竟Vert.x不熟，暂时先使用netty做为tcp服务器包，后期会继续变更

#### 打算围绕Vert.x打造一款开发便利的分布式游戏服务系统

# 目前本框架处于初步搭建阶段，数据库等都没有接入。


***另外比较头疼的是缓存层怎么搭建的问题，目前有的经验是redis+mysql，方式是读取mysql中的列表保存到redis，每次从redis反序化这大列表再拿来操作，感觉嘛，效率比较低，有大神提供其它思路的，特别欢迎指点一二***

### 数据库初步打算使用ignite ------ ignite败了，太复杂，分布式map目前还是比较难用的，集群化，ignite和hazelcast分别都失败了，连接几小时后会断连造成cpu负载高。经历换了zookeepr，vertx,zeromq等方式后，集群连接目前采用的是 vertx+zookeeper方案。缓存与数据库再做打算吧
