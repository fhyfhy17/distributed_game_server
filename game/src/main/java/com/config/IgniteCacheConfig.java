package com.config;

import com.dao.cache.PlayerDBStore;
import com.dao.cache.UserDBStore;
import com.entry.BaseEntry;
import com.entry.PlayerEntry;
import com.entry.UserEntry;
import com.enums.CacheEnum;
import com.google.common.collect.Maps;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.FactoryBuilder;
import java.util.List;
import java.util.Map;

@Configuration
public class IgniteCacheConfig {

    @Autowired
    private List<CacheConfiguration> repoList;


    @Bean
    public Map<CacheEnum, IgniteCache<Long, BaseEntry>> cacheMap() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setMetricsLogFrequency(0);
        CacheConfiguration[] cs = new CacheConfiguration[repoList.size()];
        cfg.setCacheConfiguration(repoList.toArray(cs));

        Ignite ignite = Ignition.start(cfg);

        Map<CacheEnum, IgniteCache<Long, BaseEntry>> map = Maps.newHashMap();
        for (CacheConfiguration c : cs) {

            map.put(CacheEnum.valueOf(c.getName()), ignite.cache(c.getName()));
        }
        return map;
    }

    @Bean
    public CacheConfiguration playerEntryCatche() {
        CacheConfiguration<Long, PlayerEntry> cacheCfg = new CacheConfiguration<>(CacheEnum.PlayerEntryCache.name());
        cacheCfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        cacheCfg.setReadThrough(true);
        cacheCfg.setWriteThrough(true);
        cacheCfg.setWriteBehindEnabled(true);
        cacheCfg.setWriteBehindFlushSize(0);
        cacheCfg.setWriteBehindFlushFrequency(10_000);
        cacheCfg.setWriteBehindFlushThreadCount(3);
        cacheCfg.setCacheStoreFactory(FactoryBuilder.factoryOf(PlayerDBStore.class));
        cacheCfg.setCacheMode(CacheMode.LOCAL);
        return cacheCfg;
    }

    @Bean
    public CacheConfiguration userEntryCatche() {
        CacheConfiguration<Long, UserEntry> cacheCfg = new CacheConfiguration<>(CacheEnum.UserEntryCache.name());
        cacheCfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        cacheCfg.setReadThrough(true);
        cacheCfg.setWriteThrough(true);
        cacheCfg.setWriteBehindEnabled(true);
        cacheCfg.setWriteBehindFlushSize(0);
        cacheCfg.setWriteBehindFlushFrequency(10_000);
        cacheCfg.setWriteBehindFlushThreadCount(3);
        cacheCfg.setCacheStoreFactory(FactoryBuilder.factoryOf(UserDBStore.class));
        cacheCfg.setCacheMode(CacheMode.LOCAL);
        return cacheCfg;
    }

}
