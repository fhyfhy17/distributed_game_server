package com.manager;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VertxMessage2Manager extends AbstractVerticle {

    private static Vertx vertx;
    //TODO 现在发送时就hash了，但是接收端还没有针对处理，发送与接收策略还没有想清楚，以后改


    public static void sendMessage(String queue, String msg) {
//        log.info("发送消息到集群，目标= {}", queue);
        try {
            vertx.eventBus().publish(queue, msg);
//            vertx.eventBus().send(queue, msg, rf -> {
//                if (rf.succeeded()) {
//
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        VertxMessage2Manager.vertx = vertx;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start(startFuture);
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
    }

}