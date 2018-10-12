package com.manager;

import com.pojo.Message;
import com.util.SerializeUtil;
import io.vertx.core.AbstractVerticle;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class VertxMessageManager extends AbstractVerticle {
    private static VertxMessageManager instance = null;

    @Override
    public void start() throws Exception {
        super.start();
        instance = this;
    }

    public static void sendMessage(String queue, Message message) {
        sendMessageToServer(queue, SerializeUtil.mts(message));
    }


    private static void sendMessageToServer(String queue, byte[] msg) {
        log.info("发送消息到集群，目标= {}", queue);
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