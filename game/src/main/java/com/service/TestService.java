package com.service;

import com.annotation.EventListener;
import com.dao.PlayerRepository;
import com.dao.cache.UserEntryCacheRepository;
import com.entry.PlayerEntry;
import com.entry.UserEntry;
import com.event.playerEvent.TestEvent;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.impl.internal.loaderwriter.writebehind.WriteBehind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EventListener
@Slf4j
public class TestService {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CacheManager cachemanager;

    @Autowired
    private UserEntryCacheRepository userRepository;


    @Subscribe
    public void test(TestEvent testEvent) {
//        log.info("test = {}", testEvent.getTestWord());
//        for (String cacheName : cachemanager.getCacheNames()) {
//            System.out.println("cacheName: " + cacheName);
//        }
//
//        CaffeineCache playerEntryCache = (CaffeineCache) cachemanager.getCache("PlayerEntryCache");
//        for (Map.Entry<Object, Object> entry : playerEntryCache.getNativeCache().asMap().entrySet()) {
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//            System.out.println("=====================");
//        }
    }

    @Cacheable(value = "PlayerEntryCache", sync = true, key = "'playerEntryCache.'.concat(#name)")
    public List<PlayerEntry> findByName(String name) {

        return playerRepository.findPlayerEntityByName(name);
    }

    @Cacheable(value = "UserEntryCache", sync = true, key = "'userEntryCache.'.concat(#userId)")
    public UserEntry findUserById(Long userId) {
        return userRepository.findByUid(userId);
    }

    public void insertPlayer(PlayerEntry playerEntry) {
        playerRepository.save(playerEntry);
    }

    public void aaa() {


        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
        Cache<String, String> myCache = cacheManager.createCache("myCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder().heap(100, MemoryUnit.MB))
                        .withDispatcherConcurrency(4)
                        .withLoaderWriter(new WriteBehind<String, String>() {
                            @Override
                            public void start() {

                            }

                            @Override
                            public void stop() {

                            }

                            @Override
                            public long getQueueSize() {
                                return 0;
                            }

                            @Override
                            public String load(String s) throws Exception {
                                return null;
                            }

                            @Override
                            public void write(String s, String s2) throws Exception {

                            }

                            @Override
                            public void delete(String s) throws Exception {

                            }
                        }));
    }


}
