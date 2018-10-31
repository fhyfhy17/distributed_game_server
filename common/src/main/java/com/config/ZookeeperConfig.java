package com.config;


import com.alibaba.fastjson.JSON;
import com.manager.ServerManager;
import com.pojo.ServerInfo;
import com.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ZookeeperConfig {

    @Autowired
    private ServerInfo serverInfo;

    private static final String ZK_PATH_CLUSTER_NODE = "/cluster/nodes/";
//    @Bean(name = "zookeeper")
    public void curatorFramework() {

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(300, 3, 1000);
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1")
                .namespace("io.vertx")
                .sessionTimeoutMs(3000)
                .retryPolicy(retryPolicy).build();

        curator.start();
        String uuid = ContextUtil.id + "==" + JSON.toJSONString(serverInfo);

        final PathChildrenCache childrenCache = new PathChildrenCache(curator, "/cluster/nodes", true);
        try {
            childrenCache.start();
            curator.create().withMode(CreateMode.EPHEMERAL).forPath(ZK_PATH_CLUSTER_NODE + uuid, uuid.getBytes());
        } catch (Exception e) {
            log.error("", e);
        }
        childrenCache.getListenable().addListener(
                (client, event) -> {
                    switch (event.getType()) {
                        case CHILD_ADDED:
                            ServerManager.addServerInfo(JSON.parseObject(event.getData().getPath().split("==")[1], ServerInfo.class));
//                            ServerInfoManager.addServer(event.getData().getPath());
                            break;
                        case CHILD_REMOVED:
                            ServerInfo serverInfo = JSON.parseObject(event.getData().getPath().split("==")[1], ServerInfo.class);
                            ServerManager.removeServerInfo(serverInfo.getServerId());
//                            ServerInfoManager.removeServer(event.getData().getPath());
                            break;
                        default:
                            break;
                    }
                }
        );

    }
}