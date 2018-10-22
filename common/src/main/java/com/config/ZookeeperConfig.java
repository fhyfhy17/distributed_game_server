package com.config;


import com.alibaba.fastjson.JSON;
import com.manager.ServerInfoManager;
import com.pojo.ServerInfo;
import com.util.ContextUtil;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ZookeeperConfig {

    @Autowired
    private ServerInfo serverInfo;


    @Bean(name = "zookeeper")
    public ZookeeperClusterManager curatorFramework() {
        ZookeeperClusterManager zookeeperClusterManager = null;

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(300, 3, 1000);
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1")
                .namespace("io.vertx")
                .sessionTimeoutMs(3000)
                .retryPolicy(retryPolicy).build();

        curator.start();
        String uuid = ContextUtil.id + "==" + JSON.toJSONString(serverInfo);
        zookeeperClusterManager = new ZookeeperClusterManager(curator, uuid);

        final PathChildrenCache childrenCache = new PathChildrenCache(curator, "/cluster/nodes", true);
        try {
            childrenCache.start();
        } catch (Exception e) {
            log.error("", e);
        }
        childrenCache.getListenable().addListener(
                (client, event) -> {
                    switch (event.getType()) {
                        case CHILD_ADDED:
                            ServerInfoManager.addServer(event.getData().getPath());
                            break;
                        case CHILD_REMOVED:
                            ServerInfoManager.removeServer(event.getData().getPath());
                            break;
                        default:
                            break;
                    }
                }
        );

        return zookeeperClusterManager;
    }
}