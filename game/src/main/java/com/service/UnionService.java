package com.service;

import com.annotation.EventListener;
import com.annotation.IgniteTransaction;
import com.config.CacheManager;
import com.dao.UnionRepository;
import com.entry.UnionEntry;
import com.enums.CacheEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@EventListener
@Slf4j
public class UnionService {
    @Autowired
    private UnionRepository unionRepository;


    @IgniteTransaction(cacheEnum = {CacheEnum.UnionEntryCache, CacheEnum.PlayerEntryCache})
    public void addContribute(long unionId, long contribute) {

        UnionEntry unionEntry = CacheManager.getUnionEntry(unionId);
        unionEntry.setContribution(unionEntry.getContribution() + contribute);

    }


}
