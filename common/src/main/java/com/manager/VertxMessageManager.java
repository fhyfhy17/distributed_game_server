package com.manager;

import com.pojo.Message;
import com.util.SerializeUtil;
import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertxMessageManager extends AbstractVerticle {
    private static VertxMessageManager instance = null;
    final transient static Logger log = LoggerFactory.getLogger(VertxMessageManager.class);

    @Override
    public void start() throws Exception {
        super.start();
        instance = this;
    }

    public static void sendMessage(String queue, Message message) {
        sendMessageToServer(queue, SerializeUtil.mts(message));
    }


    private static void sendMessageToServer(String queue, String msg) {
        log.info("发送消息到集群，目标= {}，消息= {}", queue, msg);
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