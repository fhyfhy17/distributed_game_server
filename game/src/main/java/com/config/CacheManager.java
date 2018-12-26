package com.config;

import com.entry.BaseEntry;
import com.entry.UnionEntry;
import com.enums.CacheEnum;
import org.apache.ignite.IgniteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
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

    public static UnionEntry getUnionEntry(long unionId) {
        IgniteCache<Long, BaseEntry> unionCache = getCache(CacheEnum.UnionEntryCache);
        UnionEntry unionEntry = (UnionEntry) unionCache.get(unionId);
        //操作 entry
        unionCache.put(unionEntry.getId(), unionEntry);


        return unionEntry;
    }

}
