package com;

import com.hazelcast.config.Config;
import com.pojo.ServerInfo;
import com.util.SerializeUtil;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public abstract class BaseVerticle {
    private Vertx vertx;
    @Autowired
    private ServerInfo serverInfo;
    @Autowired
    private Config hazelCastConfig;

    @PostConstruct
    void init() throws ExecutionException, InterruptedException {

        VertxOptions options = new VertxOptions()
                .setClusterManager(new HazelcastClusterManager(hazelCastConfig));
        CompletableFuture<Vertx> future = new CompletableFuture<>();
        Vertx.clusteredVertx(options, ar -> {
            if (ar.succeeded()) {
                future.complete(ar.result());
            } else {
                future.completeExceptionally(ar.cause());
            }
        });
        vertx = future.get();
        start();
    }


    public void start() {
        log.info("启动vertx");
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer(serverInfo.getServerId(),
                msg -> getReceiver().onReceive(SerializeUtil.stm((byte[]) msg.body())));
    }

    @Bean(destroyMethod = "")
    Vertx vertx() {
        return vertx;
    }

    @PreDestroy
    void close() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        vertx.close(ar -> future.complete(null));
        future.get();
    }

    public abstract BaseReceiver getReceiver();
}
