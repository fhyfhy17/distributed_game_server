package com.dao.cache;

import com.dao.UserRepository;
import com.entry.UserEntry;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;

@Repository
public class UserDBStore extends CacheStoreAdapter<Long, UserEntry> {

    @Autowired
    private UserRepository userRepo;
    private static Logger logger = LoggerFactory.getLogger(UserDBStore.class);

    @Override
    public UserEntry load(Long key) throws CacheLoaderException {

        logger.info(String.valueOf(userRepo));
        return userRepo.findById(key).orElse(null);
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends UserEntry> entry) throws CacheWriterException {
        UserEntry userEntry = entry.getValue();
        logger.info(String.valueOf(userRepo));
        userRepo.save(userEntry);
    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        logger.info(String.valueOf(userRepo));
        userRepo.deleteById((Long) key);
    }

}