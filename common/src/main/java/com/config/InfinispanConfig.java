package com.config;


import com.Constant;
import com.manager.ServerInfoManager;
import com.pojo.ServerInfo;
import com.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;
import org.infinispan.remoting.transport.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class InfinispanConfig {

    @Autowired
    private ServerInfo serverInfo;

    @Bean
    @Primary
    public DefaultCacheManager config() throws IOException {
        InputStream xmlInput = this.getClass().getClassLoader().getResourceAsStream("jgroup-tcp.xml");
        DefaultCacheManager defaultCacheManager = new DefaultCacheManager(xmlInput, false);

        defaultCacheManager.addListener(new ServerRemoveListener());
        defaultCacheManager.start();

        Cache<Address, ServerInfo> cache = defaultCacheManager.getCache(Constant.INFINISPAN_CLUSTER_CACHE_NAME);
        for (Map.Entry<Address, ServerInfo> entry : cache.entrySet()) {
            ServerInfoManager.addServer(entry.getValue(), entry.getKey());
        }

        cache.addListener(new ServerAddListener());
        cache.put(defaultCacheManager.getAddress(), serverInfo);

        return defaultCacheManager;
    }
}

@Listener(clustered = true)
class ServerAddListener {

    @CacheEntryCreated
    public void create(CacheEntryCreatedEvent<Address, ServerInfo> event) {
        String serverId = event.getValue().getServerId();
        if (!ServerInfoManager.ifCached(serverId)) {
            ServerInfoManager.addServer(event.getValue(), event.getKey());
        }
    }


}


@Listener(clustered = true)
class ServerRemoveListener {
    @ViewChanged
    public void viewChanged(ViewChangedEvent event) {
        List<Address> newMembers = event.getNewMembers();
        List<Address> oldMembers = event.getOldMembers();
        oldMembers.stream()
                .filter(address -> !newMembers.contains(address))
                .forEach(
                        address -> {
                            ServerInfoManager.removeServer(address);
                            Cache<Address, ServerInfo> cache = SpringUtils.getBean(DefaultCacheManager.class).getCache(Constant.INFINISPAN_CLUSTER_CACHE_NAME);
                            cache.evict(address);
                        }

                );
    }
}
