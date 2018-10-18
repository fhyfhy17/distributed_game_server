package com.config;


import com.Constant;
import com.alibaba.fastjson.JSON;
import com.pojo.ServerInfo;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ZookeeperConfig {

    @Autowired
    private ServerInfo serverInfo;


    @Bean(name = "zookeeper")
    public JsonObject config() {
        JsonObject zkConfig = new JsonObject();
        zkConfig.put("zookeeperHosts", "127.0.0.1");// zk host 地址
        zkConfig.put("rootPath", "io.vertx");// 根路径
        zkConfig.put("retry", new JsonObject() // 重试策略
                .put("initialSleepTime", 3000)
                .put("maxTimes", 3));
        zkConfig.put(Constant.SERVER_INFO, JSON.toJSONString(serverInfo));

        return zkConfig;
    }
}