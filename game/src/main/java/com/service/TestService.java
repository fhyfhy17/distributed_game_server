package com.service;

import com.annotation.EventListener;
import com.dao.PlayerRepository;
import com.dao.cache.UserEntryCacheRepository;
import com.entry.PlayerEntry;
import com.entry.UserEntry;
import com.event.playerEvent.TestEvent;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
        log.info("test = {}", testEvent.getTestWord());
        for (String cacheName : cachemanager.getCacheNames()) {
            System.out.println("cacheName: " + cacheName);
        }

        CaffeineCache playerEntryCache = (CaffeineCache) cachemanager.getCache("PlayerEntryCache");
        for (Map.Entry<Object, Object> entry : playerEntryCache.getNativeCache().asMap().entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            System.out.println("=====================");
        }
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

}
