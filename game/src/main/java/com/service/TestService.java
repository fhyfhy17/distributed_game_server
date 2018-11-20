package com.service;

import com.annotation.EventListener;
import com.config.CacheConfig;
import com.config.CacheConstant;
import com.dao.PlayerRepository;
import com.entry.PlayerEntry;
import com.event.TestEvent;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EventListener
@Slf4j
public class TestService extends BaseService<PlayerEntry, String> {
    @Autowired
    PlayerRepository playerRepository;

    @Override
    MongoRepository<PlayerEntry, String> getRepository() {
        return playerRepository;
    }

    @Subscribe
    public void test(TestEvent testEvent) {
        log.info("test = {}", testEvent.getTestWord());
    }
    @Cacheable(value= "PlayerEntryCache", sync=true,key = )
    public List<PlayerEntry> findByName(String name) {
        return playerRepository.findByName(name);
    }

}
