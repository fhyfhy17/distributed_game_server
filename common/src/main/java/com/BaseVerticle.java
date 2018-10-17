package com;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.net.node.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public abstract class BaseVerticle {

    @Autowired
    private Config hazelCastConfig;
    @Autowired
    private Node node;

    @PostConstruct
    void init() {
        Hazelcast.newHazelcastInstance(hazelCastConfig);
        publishService();
    }


    protected void publishService() {
        // 发布服务信息
        node.setBaseReceiver(getReceiver());
        new Thread(() -> node.start()).start();
    }

    public abstract BaseReceiver getReceiver();
}
