package com.config;


import com.Constant;
import com.manager.ServerInfoManager;
import com.pojo.ServerInfo;
import com.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.BinaryConfiguration;
import org.apache.ignite.configuration.DeploymentMode;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.events.DiscoveryEvent;
import org.apache.ignite.events.EventType;
import org.apache.ignite.lang.IgnitePredicate;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@Slf4j
public class IgniteConfig {

    @Autowired
    private ServerInfo serverInfo;

    @Bean("myIg")
    public Ignite igConfig() {
        IgniteConfiguration cfg = new IgniteConfiguration();

        cfg.setUserAttributes(Collections.singletonMap(Constant.SERVER_INFO, serverInfo));
        cfg.setIgniteInstanceName(ContextUtil.id);
        cfg.setDeploymentMode(DeploymentMode.PRIVATE)
//                .setPeerClassLoadingEnabled(true)
                .setBinaryConfiguration(new BinaryConfiguration()
                        .setCompactFooter(true))
                .setCommunicationSpi(new TcpCommunicationSpi()
                        .setLocalAddress("localhost"))
                .setDiscoverySpi(new TcpDiscoverySpi()
                        .setIpFinder(new TcpDiscoveryVmIpFinder()
                                .setAddresses(Arrays.asList("127.0.0.1:47500..47509")
                                )
                        )
                )

                .setMetricsLogFrequency(0);
        Ignite start = Ignition.start(cfg);
        for (ClusterNode node : start.cluster().forRemotes().nodes()) {
            ServerInfoManager.addServer(node.attribute(Constant.SERVER_INFO));
        }
        start.events().localListen( event -> {
            DiscoveryEvent e = (DiscoveryEvent)event;
            if (e.type() == EventType.EVT_NODE_JOINED) {
                ServerInfoManager.addServer(e.eventNode().attribute(Constant.SERVER_INFO));
                return true;
            }

            if (e.type() == EventType.EVT_NODE_LEFT || e.type() == EventType.EVT_NODE_FAILED) {
                ServerInfoManager.removeServer(e.eventNode().attribute(Constant.SERVER_INFO));
                return true;
            }

            return false;
        }, EventType.EVT_NODE_FAILED, EventType.EVT_NODE_LEFT, EventType.EVT_NODE_JOINED);

        return start;
    }
}