package com.manager;

import com.pojo.Message;
import com.util.SerializeUtil;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VertxMessageManager {

    private static Vertx vertx;

    public static void sendMessage(String queue, Message message) {
        sendMessageToServer(queue, SerializeUtil.mts(message));
    }


    private static void sendMessageToServer(String queue, byte[] msg) {
        log.info("发送消息到集群，目标= {}", queue);
        try {
            vertx.eventBus().send(queue, msg, rf -> {
                if (rf.succeeded()) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setVertx(Vertx vertx) {
        VertxMessageManager.vertx = vertx;
    }
}