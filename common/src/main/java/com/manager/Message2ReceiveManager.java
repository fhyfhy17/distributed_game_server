package com.manager;

import com.BaseReceiver;
import com.util.ContextUtil;
import com.util.CountUtil;
import com.util.SpringUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Message2ReceiveManager extends AbstractVerticle {

    private static Vertx vertx;
    private BaseReceiver receiver = SpringUtils.getBeansOfType(BaseReceiver.class).values().iterator().next();

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);
        Message2ReceiveManager.vertx = vertx;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start(startFuture);
        CountUtil.start();
        String name = EventBusNameCreater.getName(ContextUtil.id + "a");
        System.out.println("启动的接收器:" + name);
        vertx.eventBus().consumer(name,
                msg -> {
                    CountUtil.count();
                });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
    }
}