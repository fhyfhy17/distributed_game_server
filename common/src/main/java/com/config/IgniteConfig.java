package com.config;


import com.Constant;
import com.manager.ServerInfoManager;
import com.pojo.ServerInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.events.EventType;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.spi.eventstorage.NoopEventStorageSpi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@Slf4j
public class IgniteConfig {

    @Autowired
    private ServerInfo serverInfo;

    @Bean("myIg")
    public Ignite igConfig() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
// Set initial IP addresses.
// Note that you can optionally specify a port or a port range.
        ipFinder.setAddresses(Arrays.asList("192.168.1.28", "10.0.107.233"));
        spi.setIpFinder(ipFinder);

// Override default discovery SPI.
        cfg.setDiscoverySpi(spi);
// Start Ignite node.
        NoopEventStorageSpi eventStorageSpi = new NoopEventStorageSpi();

        eventStorageSpi.localEvents(event -> {
            ServerInfo serverInfo = event.node().attribute(Constant.SERVER_INFO);
            if (event.type() == EventType.EVT_NODE_FAILED || event.type() == EventType.EVT_NODE_LEFT) {
                ServerInfoManager.printServerStatus(serverInfo, false);
                return true;
            }
            if (event.type() == EventType.EVT_NODE_JOINED) {
                ServerInfoManager.printServerStatus(serverInfo, true);
                return true;
            }
            return true;
        }, EventType.EVTS_DISCOVERY_ALL);
    }
})

        cfg.setEventStorageSpi(eventStorageSpi);
        cfg.setUserAttributes(Collections.singletonMap(Constant.SERVER_INFO,serverInfo));
        Ignite start=Ignition.start(cfg);

        // 监听打印信息
        start.events(start.cluster().forLocal()).remoteListen(null,(IgnitePredicate<DiscoveryEvent>)event->{

        return start;
        }
        }
