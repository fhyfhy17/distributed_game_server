package com.config;

import com.dao.cache.PlayerDBStore;
import com.dao.cache.UnionDBStore;
import com.dao.cache.UserDBStore;
import com.entry.PlayerEntry;
import com.entry.UnionEntry;
import com.entry.UserEntry;
import com.enums.CacheEnum;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.FactoryBuilder;

@Configuration
public class IgniteCacheConfig {

    @Bean
    public CacheConfiguration playerEntryCache() {
        CacheConfiguration<Long, PlayerEntry> cacheCfg = new CacheConfiguration<>(CacheEnum.PlayerEntryCache.name());
        cacheCfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        cacheCfg.setReadThrough(true);
        cacheCfg.setWriteThrough(true);
        cacheCfg.setWriteBehindEnabled(true);
        cacheCfg.setWriteBehindFlushSize(1000);
        cacheCfg.setWriteBehindFlushFrequency(10_000);
        cacheCfg.setWriteBehindFlushThreadCount(1);
        cacheCfg.setCacheStoreFactory(FactoryBuilder.factoryOf(PlayerDBStore.class));
        cacheCfg.setCacheMode(CacheMode.LOCAL);
        return cacheCfg;
    }

    @Bean
    public CacheConfiguration userEntryCache() {

        CacheConfiguration<Long, UserEntry> cacheCfg = new CacheConfiguration<>(CacheEnum.UserEntryCache.name());
        cacheCfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        cacheCfg.setReadThrough(true);
        cacheCfg.setWriteThrough(true);
        cacheCfg.setWriteBehindEnabled(true);
        cacheCfg.setWriteBehindFlushSize(1001);
        cacheCfg.setWriteBehindFlushFrequency(10_000);
        cacheCfg.setWriteBehindFlushThreadCount(1);
        cacheCfg.setCacheStoreFactory(FactoryBuilder.factoryOf(UserDBStore.class));
        cacheCfg.setCacheMode(CacheMode.LOCAL);
        return cacheCfg;
    }

    @Bean
    public CacheConfiguration unionEntryCache() {

        CacheConfiguration<Long, UnionEntry> cacheCfg = new CacheConfiguration<>(CacheEnum.UnionEntryCache.name());
        cacheCfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        cacheCfg.setReadThrough(true);
        cacheCfg.setWriteThrough(true);
        cacheCfg.setCacheStoreFactory(FactoryBuilder.factoryOf(UnionDBStore.class));
        cacheCfg.setCacheMode(CacheMode.PARTITIONED);
        cacheCfg.setBackups(3);
        return cacheCfg;
    }

}
