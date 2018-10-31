package com.manager;

import com.pojo.Message;
import com.util.SerializeUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VertxMessageManager extends AbstractVerticle {

    private static Vertx vertx;

    public static void sendMessage(String queue, Message message) {
        sendMessageToServer(queue, SerializeUtil.mts(message));
    }


    private static void sendMessageToServer(String queue, byte[] msg) {
//        log.info("发送消息到集群，目标= {}", queue);
        try {
            vertx.eventBus().publish(queue,msg);
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
        VertxMessageManager.vertx = vertx;
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