package com.config;

import com.entry.BaseEntry;
import com.enums.CacheEnum;
import org.apache.ignite.IgniteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CacheManager {
    @Autowired
    private static Map<CacheEnum, IgniteCache<Long, BaseEntry>> cacheMap;

    public static Map<CacheEnum, IgniteCache<Long, BaseEntry>> getCacheMap() {
        return cacheMap;
    }

    public static IgniteCache<Long, BaseEntry> getCache(CacheEnum cacheEnum) {
        return cacheMap.get(cacheEnum);
    }

    @Autowired
    public void setCacheMap(Map<CacheEnum, IgniteCache<Long, BaseEntry>> cacheMap) {
        CacheManager.cacheMap = cacheMap;
    }
}
