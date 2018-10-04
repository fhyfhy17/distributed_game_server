# distributed_game_server

-----

## 分布式Java游戏服务器

### 所用框架
- Vert.x       构建服务间消息发送系统
- Hazelcast       分布式发现与注册，分布式map，分布式锁
- Springboot       采用非web方式启动，注解编程，方便嫁接Spring系列和其它第三方框架
- Kryo，fastjson       Vert.x传递消息为String，此两个做为序列化工具
- protobuf3       协议
- hutool       方便的工具包，顺便学学怎么写各类工具代码
- netty       tcp连接，毕竟Vert.x不熟，暂时先使用netty做为tcp服务器包，后期会继续变更

#### 打算围绕Vert.x打造一款开发便利的分布式游戏服务系统

# 目前本框架处于初步搭建阶段，数据库等都没有接入。

***另外比较头疼的是缓存层怎么搭建的问题，目前有的经验是redis+mysql，方式是读取mysql中的列表保存到redis，每次从redis反序化这大列表再拿来操作，感觉嘛，效率比较低，有大神提供其它思路的，特别欢迎指点一二***
