package com.dao.cache;

import com.dao.PlayerRepository;
import com.entry.PlayerEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@Repository
@NoRepositoryBean
@CacheConfig(cacheNames = "PlayerEntryCache")
public class PlayerEntryCacheRepository {

    @Autowired
    private PlayerRepository playerRepository;

    @Cacheable
    public PlayerEntry findById(String id) {
        return playerRepository.findById(id).orElse(null);
    }

    @CacheEvict(key = "#playerEntry.id")
    public PlayerEntry save(PlayerEntry playerEntry) {
        return playerRepository.save(playerEntry);
    }

    @CacheEvict(key = "#playerEntry.id")
    public void delete(PlayerEntry playerEntry) {
        playerRepository.delete(playerEntry);
    }

    @CacheEvict
    public void delete(String id) {
        playerRepository.deleteById(id);
    }
}