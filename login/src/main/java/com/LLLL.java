package com;

import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LLLL {
    @Autowired
    private Vertx vertx;

    @PostConstruct
    public void say() {
        vertx.eventBus().send("aaa", "你大爷啊！！！！");
    }
}
