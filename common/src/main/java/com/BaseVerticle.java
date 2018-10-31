package com;

import com.config.ZookeeperConfig;
import com.net.node.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public abstract class BaseVerticle {

    @Autowired
    private Node node;
    @Autowired
    private ZookeeperConfig zookeeperConfig;
    @PostConstruct
    void init() {
        zookeeperConfig.curatorFramework();
        publishService();
    }


    protected void publishService() {
        // 发布服务信息
        node.setBaseReceiver(getReceiver());
        new Thread(() -> node.start()).start();
    }

    public abstract BaseReceiver getReceiver();
}
