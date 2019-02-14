package com.manager;

import com.pojo.Message;
import com.util.SerializeUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

import static com.Constant.MESSAGE_RECEIVE_DEPLOY_NUM;

@Slf4j
public class VertxMessageManager extends AbstractVerticle {

    private static Vertx[] vertxs = new Vertx[MESSAGE_RECEIVE_DEPLOY_NUM];
    private static AtomicInteger count = new AtomicInteger(-1);

    //TODO 现在发送时就hash了，但是接收端还没有针对处理，发送与接收策略还没有想清楚，以后改
    public static void sendMessage(String queue, Message message) {
        int count = (int) (message.getUid() % MESSAGE_RECEIVE_DEPLOY_NUM);
        sendMessageToServer(queue + "-" + count, SerializeUtil.mts(message), count);
    }


    private static void sendMessageToServer(String queue, byte[] msg, int count) {
//        log.info("发送消息到集群，目标= {}", queue);
        try {
//            System.out.println("发送的vertx :"+count);
            vertxs[count].eventBus().publish(queue, msg);
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
        int i = count.incrementAndGet();
        System.out.println("启动发送器 " + i);
        VertxMessageManager.vertxs[i] = vertx;
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