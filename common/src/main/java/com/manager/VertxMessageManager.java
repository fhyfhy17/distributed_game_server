package com.manager;

import com.net.msg.Message;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class VertxMessageManager extends AbstractVerticle {
    private static VertxMessageManager instance = null;
    final transient static Logger log = LoggerFactory.getLogger(VertxMessageManager.class);

    @Override
    public void start() throws Exception {
        super.start();
        instance = this;
    }

    public static void sendMessage(String queue, Message message) {
        sendMessageToServer(queue, message.objToString());
    }


    private static void sendMessageToServer(String queue, String msg) {
        log.info("发送消息到集群，队列：{}，消息：{}", queue, msg);
        try {
            instance.vertx.eventBus().send(queue, msg, rf -> {
                if (rf.succeeded()) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}