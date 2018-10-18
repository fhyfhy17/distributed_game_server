package com;

import io.vertx.core.Vertx;
import io.vertx.core.spi.cluster.ClusterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class GGGG {
    @Autowired
    private Vertx vertx;
    @Autowired
    private ClusterManager clusterManager;

    @PostConstruct
    public void say(){
        System.out.println(111);
        vertx.eventBus().consumer("aaa",message -> {
            System.out.println(message.body());
            for (String s : clusterManager.getNodes()) {
                System.out.println(s);
            }
        });
    }
}
