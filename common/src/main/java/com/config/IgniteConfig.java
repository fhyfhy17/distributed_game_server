package com.config;

import com.Constant;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.manager.ServerInfoManager;
import com.pojo.ServerInfo;
import com.util.ContextUtil;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.events.DiscoveryEvent;
import org.apache.ignite.events.Event;
import org.apache.ignite.events.EventType;
import org.apache.ignite.lang.IgnitePredicate;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Configuration
@EnableIgniteRepositories
public class IgniteConfig {

    @Autowired
    private ServerInfo serverInfo;

    @Bean(name = "myignite")
    public Ignite ignite() {
//        Ignite ignite = Ignition.start("myignite.xml");

        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setPeerClassLoadingEnabled(true);
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName("igniteClusterNodeCache");
        cacheConfiguration.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        cacheConfiguration.setCacheMode(CacheMode.REPLICATED);
        cacheConfiguration.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        cfg.setCacheConfiguration(cacheConfiguration);


        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        discoverySpi.setLocalPort(47500);   //端口
        discoverySpi.setLocalPortRange(10);   //节点个数
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        List<String> addresses = new ArrayList<>();
        addresses.add("127.0.0.1:47500..47509");     //地址
        ipFinder.setAddresses(addresses);
        discoverySpi.setIpFinder(ipFinder);
        cfg.setDiscoverySpi(discoverySpi);
        cfg.setUserAttributes(Collections.singletonMap(Constant.SERVER_INFO,serverInfo));
        cfg.setIgniteInstanceName(ContextUtil.id);
        Ignite ignite = Ignition.start(cfg);


//        ignite.configuration().setUserAttributes(Collections.singletonMap(Constant.SERVER_INFO,serverInfo));
        // 监听打印信息
        ignite.events(ignite.cluster().forLocal()).remoteListen(null, (IgnitePredicate<DiscoveryEvent>) event -> {
            ServerInfo serverInfo =event.eventNode().attribute(Constant.SERVER_INFO);
            if (event.type() == EventType.EVT_NODE_FAILED||event.type()==EventType.EVT_NODE_LEFT) {
                ServerInfoManager.printServerStatus(serverInfo, false);
                return true;
            }
            if (event.type() == EventType.EVT_NODE_JOINED) {
                ServerInfoManager.printServerStatus(serverInfo, true);
                return true;
            }
            return true;
        }, EventType.EVTS_DISCOVERY_ALL);
        return ignite;
    }
}
