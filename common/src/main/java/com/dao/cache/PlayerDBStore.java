package com.dao.cache;

import com.dao.PlayerRepository;
import com.entry.PlayerEntry;
import com.util.SpringUtils;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.springframework.stereotype.Repository;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.util.List;

@Repository
public class PlayerDBStore extends CacheStoreAdapter<Long, PlayerEntry> {

    @Override
    public PlayerEntry load(Long key) throws CacheLoaderException {
        PlayerRepository playerRepository = SpringUtils.getBean(PlayerRepository.class);
        return playerRepository.findById(key).orElse(null);
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends PlayerEntry> entry) throws CacheWriterException {
        PlayerRepository playerRepository = SpringUtils.getBean(PlayerRepository.class);
        PlayerEntry playerEntry = entry.getValue();
        playerRepository.save(playerEntry);
    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        PlayerRepository playerRepository = SpringUtils.getBean(PlayerRepository.class);
        playerRepository.deleteById((Long) key);
    }
}