package com;

import com.hazelcast.config.Config;
import com.hazelcast.core.*;
import com.hazelcast.map.listener.EntryAddedListener;
import com.manager.ServerInfoManager;
import com.manager.VertxMessageManager;
import com.pojo.ServerInfo;
import com.util.SerializeUtil;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public abstract class BaseVerticle {
    private Vertx vertx;
    @Autowired
    private ServerInfo serverInfo;
    @Autowired
    @Qualifier("hazelcast")
    private Config config;
    @PostConstruct
    void init() throws ExecutionException, InterruptedException {

        VertxOptions options = new VertxOptions()
                .setClusterManager(new HazelcastClusterManager(config));
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

        vertx.deployVerticle(VertxMessageManager.class, new DeploymentOptions().setWorker(true).setInstances(3));
        eventBus.consumer(serverInfo.getServerId(),
                msg -> getReceiver().onReceive(SerializeUtil.stm(msg.body().toString())));

        publishService();

    }

    protected void publishService() {
        // 发布服务信息

        HazelcastInstance ins = Hazelcast.getHazelcastInstanceByName(serverInfo.getServerId());
        // 获取其他服务的信息缓存到本地
        IMap<String, ServerInfo> map = ins.getMap(Constant.SERVER_MAP);
        Set<Member> members = ins.getCluster().getMembers();
        for (Member member : members) {
            if (member.localMember()) {
                // 发布本节点
                map.put(member.getUuid(), serverInfo);
            } else {
                // 读取其他节点
                Set<Map.Entry<String, ServerInfo>> entries = map.entrySet();
                for (Map.Entry<String, ServerInfo> entry : entries) {
                    ServerInfoManager.addServerInfo(entry.getValue());
                }

            }
        }
        map.addEntryListener((EntryAddedListener<String, ServerInfo>) event -> {
            // 监听其他节点加入
            ServerInfo value = event.getValue();
            ServerInfoManager.addServerInfo(value);
            log.info("来了一个节点= {}", value.getServerId());
        }, true);


        ins.getCluster().addMembershipListener(new MembershipListener() {
            @Override
            public void memberAdded(MembershipEvent membershipEvent) {

            }

            @Override
            public void memberRemoved(MembershipEvent membershipEvent) {
                if (!membershipEvent.getMember().localMember()) {
                    Member member = membershipEvent.getMember();
                    log.info("集群中节点关闭 = {}", member);
                    ServerInfo remove = map.remove(membershipEvent.getMember().getUuid());
                    ServerInfoManager.removeServerInfo(remove.getServerId());
                }
            }


            @Override
            public void memberAttributeChanged(MemberAttributeEvent memberAttributeEvent) {
            }

        });

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
