package com.service;

import com.annotation.EventListener;
import com.config.CacheManager;
import com.dao.PlayerRepository;
import com.dao.UserRepository;
import com.entry.PlayerEntry;
import com.entry.UserEntry;
import com.enums.CacheEnum;
import com.event.playerEvent.TestEvent;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EventListener
@Slf4j
public class TestService {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private List<CacheConfiguration> repoList;


    @Subscribe
    public void test(TestEvent testEvent) {
        for (CacheConfiguration cacheConfiguration : repoList) {
            System.out.println(cacheConfiguration.getName());
        }

        testEvent.getTestWord();

        IgniteCache cache = CacheManager.getCache(CacheEnum.PlayerEntryCache);
        System.out.println(cache.get(testEvent.getTestWord()));
        System.out.println(cache.get(testEvent.getTestWord()));
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

    //    @Cacheable(value = "PlayerEntryCache", sync = true, key = "'playerEntryCache.'.concat(#name)")
    public List<PlayerEntry> findByName(String name) {

        return playerRepository.findPlayerEntityByName(name);
    }

    //    @Cacheable(value = "UserEntryCache", sync = true, key = "'userEntryCache.'.concat(#userId)")
    public UserEntry findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public PlayerEntry insertPlayer(PlayerEntry playerEntry) {
        PlayerEntry save = playerRepository.save(playerEntry);
        return save;
    }

    public static void main(String[] args) throws InterruptedException {

        aaa();
    }

    public static void aaa() throws InterruptedException {


        // To start ignite with desired configuration uncomment the appropriate line.

//
//            System.out.println();
//            System.out.println(">>> Cache store example started.");
//
//            IgniteConfiguration cfg = new IgniteConfiguration();
//            CacheConfiguration<String, PlayerEntry> cacheCfg = new CacheConfiguration<>("aaaa");
//            cacheCfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
//            cacheCfg.setReadThrough(true);
//            cacheCfg.setWriteThrough(true);
//            cacheCfg.setWriteBehindEnabled(true);
//            cacheCfg.setWriteBehindFlushSize(0);
//            cacheCfg.setWriteBehindFlushFrequency(10_000);
//            cacheCfg.setWriteBehindFlushThreadCount(3);
//            cacheCfg.setCacheStoreFactory(FactoryBuilder.factoryOf(PlayerDBStore.class));
//            cacheCfg.setCacheMode(CacheMode.LOCAL);
//            cfg.setCacheConfiguration(cacheCfg);
//            Ignite ignite = Ignition.start(cfg);
//
//        IgniteCache<Long, String> aaa = ignite.getOrCreateCache("aaa");
//        aaa.put(1L,"BBB");
//
//
//        System.out.println(aaa.get(1L));

    }
}
