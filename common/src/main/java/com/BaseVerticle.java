package com;

import com.alibaba.fastjson.JSON;
import com.manager.MessageReceiveManager;
import com.manager.VertxMessageManager;
import com.pojo.ServerInfo;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.core.spi.cluster.NodeListener;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("zookeeper")
    private JsonObject zookeeperConfig;
    ZookeeperClusterManager clusterManager;
    @Autowired
    private ServerInfo serverInfo;


    @PostConstruct
    void init() throws ExecutionException, InterruptedException {
         clusterManager = new ZookeeperClusterManager(zookeeperConfig);
         clusterManager.nodeListener(new NodeListener() {
             @Override
             public void nodeAdded(String nodeID) {
                 System.out.println(nodeID);
//                 for (String s : clusterManager.getNodes()) {
//                     System.out.println("!!!!!!!!!!!   "+s);
//                 }
//                 if(clusterManager.getNodeID().equals(nodeID)){
//                     try {
//                         clusterManager.getCuratorFramework()
//                                 .setData()
//                                 .forPath("/cluster/nodes/"+nodeID,JSON.toJSONString(serverInfo).getBytes());
//                     } catch (Exception e) {
//                         e.printStackTrace();
//                     }
//                 }else{
//                     System.out.println("新加入 == "+nodeID);
//                 }


             }

             @Override
             public void nodeLeft(String nodeID) {

             }
         });
        VertxOptions options = new VertxOptions()
                .setClusterManager(clusterManager);

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
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setWorker(true);
        deploymentOptions.setInstances(5);
        //部署发送
        vertx.deployVerticle(VertxMessageManager.class, deploymentOptions);
        //部署接收
        vertx.deployVerticle(MessageReceiveManager.class, deploymentOptions);

    }

    @Bean(destroyMethod = "")
    Vertx vertx() {
        return vertx;
    }

    @Bean
    ZookeeperClusterManager clusterManager(){
        return clusterManager;
    }

    @PreDestroy
    void close() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = new CompletableFuture<>();
        vertx.close(ar -> future.complete(null));
        future.get();
    }

}
