package com.config;


import com.alibaba.fastjson.JSON;
import com.manager.ServerInfoManager;
import com.pojo.ServerInfo;
import com.util.ContextUtil;
import io.vertx.core.spi.cluster.NodeListener;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
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

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 3, 10000);
        CuratorFramework curator = CuratorFrameworkFactory.builder()
//                .defaultData(JSON.toJSONString(serverInfo).getBytes())
                .connectString("127.0.0.1")
                .namespace("io.vertx")
                .sessionTimeoutMs(20000)
                .retryPolicy(retryPolicy).build();

        curator.start();
        String uuid = ContextUtil.id + "==" + JSON.toJSONString(serverInfo);
        zookeeperClusterManager = new ZookeeperClusterManager(curator, uuid);
        curator.getCuratorListenable().addListener(new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println(event);
            }
        });

//        TreeCache cache = new TreeCache(curator, "/io.vertx");
//        cache.getListenable().addListener(new TreeCacheListener() {
//            @Override
//            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
//                System.out.println("事件类型：" + event.getType());
//                System.out.println(   " | 路径：" + (null != event.getData() ? event.getData().getPath() : null));
//            }
//        });
//        try {
//            cache.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        final PathChildrenCache childrenCache = new PathChildrenCache(curator, "/io.vertx/cluster/nodes", true);
//        try {
//            childrenCache.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//
//        childrenCache.getListenable().addListener(
//                (client, event) -> {
//                    switch (event.getType()) {
//                        case CHILD_ADDED:
//
//                            System.out.println("CHILD_ADDED: " + event.getData().getPath());
//                            ServerInfoManager.addServer(event.getData().getPath());
//                            break;
//                        case CHILD_REMOVED:
//                            System.out.println("CHILD_REMOVED: " + event.getData().getPath());
//                            ServerInfoManager.removeServer(event.getData().getPath());
//                            break;
//                        case CHILD_UPDATED:
//                            System.out.println("CHILD_UPDATED: " + event.getData().getPath());
//                            break;
//                        default:
//                            break;
//                    }
//                }
//        );

        zookeeperClusterManager.nodeListener(new NodeListener() {
            @Override
            public void nodeAdded(String nodeID) {
                ServerInfoManager.addServer(nodeID);
            }

            @Override
            public void nodeLeft(String nodeID) {
                ServerInfoManager.removeServer(nodeID);
            }
        });
        return zookeeperClusterManager;
    }
}