package com.manager;

import com.net.msg.RemoteNode;
import com.pojo.Message;
import com.pojo.ServerInfo;
import com.util.SerializeUtil;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VertxMessageManager {


    public static void sendMessage(String queue, Message message) {
        sendMessageToServer(queue, SerializeUtil.mts(message));
    }


    private static void sendMessageToServer(String queue, byte[] msg) {
        RemoteNode remote = ServerInfoManager.getRemote(queue);
        remote.sendReqMsg(msg);
//        log.info("发送消息到集群，目标= {}", queue);
//        try {
//            vertx.eventBus().send(queue, msg, rf -> {
//                if (rf.succeeded()) {
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


}