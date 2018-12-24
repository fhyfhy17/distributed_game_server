package com.dao.cache;

import com.dao.UnionRepository;
import com.entry.UnionEntry;
import com.util.SpringUtils;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.springframework.stereotype.Repository;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;

@Repository
public class UnionDBStore extends CacheStoreAdapter<Long, UnionEntry> {

    @Override
    public UnionEntry load(Long key) throws CacheLoaderException {
        UnionRepository unionRepository = SpringUtils.getBean(UnionRepository.class);
        return unionRepository.findById(key).orElse(null);
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends UnionEntry> entry) throws CacheWriterException {
        UnionRepository unionRepository = SpringUtils.getBean(UnionRepository.class);
        UnionEntry unionEntry = entry.getValue();
        unionRepository.save(unionEntry);
    }

    @Override
    public void delete(Object key) throws CacheWriterException {
        UnionRepository unionRepository = SpringUtils.getBean(UnionRepository.class);
        unionRepository.deleteById((Long) key);
    }
}