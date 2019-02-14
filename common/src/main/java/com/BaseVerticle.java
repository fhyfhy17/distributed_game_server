package com;

import com.manager.MessageReceiveManager;
import com.manager.VertxMessageManager;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.ignite.IgniteClusterManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.Constant.MESSAGE_RECEIVE_DEPLOY_NUM;

@Component
@Slf4j
public abstract class BaseVerticle {
    private Vertx vertx;

    @Autowired
    private Ignite ignite;

    @PostConstruct
    void init() throws ExecutionException, InterruptedException {

        VertxOptions options = new VertxOptions()
                .setClusterManager(new IgniteClusterManager(ignite));

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

        DeploymentOptions deploymentOptions = new DeploymentOptions().setInstances(MESSAGE_RECEIVE_DEPLOY_NUM).setWorker(true);
        //部署发送1
        vertx.deployVerticle(VertxMessageManager.class, deploymentOptions);

        //部署接收5
        vertx.deployVerticle(MessageReceiveManager.class, deploymentOptions);

        //部署ignite消息
//        IgniteMessaging rmtMsg = ignite.message(ignite.cluster().forRemotes());
//
//
//        CountUtil.start();
//        rmtMsg.localListen(ContextUtil.ti, (nodeId, msg) -> {
//            CountUtil.count();
//            return true; // Return true to continue listening.
//        });

//        Node node = new Node();
//        node.setBaseReceiver(SpringUtils.getBeansOfType(BaseReceiver.class).values().iterator().next());
//        new Thread(node::start).start();
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

}
