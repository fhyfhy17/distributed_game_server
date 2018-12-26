package com.controller;

import com.config.CacheManager;
import com.dao.PlayerRepository;
import com.dao.UserRepository;
import com.entry.BaseEntry;
import com.entry.PlayerEntry;
import com.enums.CacheEnum;
import com.mongoListener.SaveEventListener;
import com.util.IdCreator;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.cache.Cache;
import java.util.Iterator;

@RestController
public class WebTestEnter {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SaveEventListener saveEventListener;
    @Autowired
    private Ignite ignite;


    @RequestMapping("/test/seq")
    public void testSeq() {

//        IgniteAtomicSequence seq = ignite.atomicSequence(
//                "abctest", // Sequence name.
//                0,       // Initial value for sequence.
//                true     // Create if it does not exist.
//        );
//
//        seq.getAndAdd(2);
//        long l = seq.incrementAndGet();

        IgniteCache<Long, BaseEntry> cache = CacheManager.getCache(CacheEnum.PlayerEntryCache);
        try (Transaction tx = ignite.transactions().txStart()) {


            tx.commit();
        }


        cache.invoke(1071010079177838592L, (entry, args) -> {
            PlayerEntry value = (PlayerEntry) entry.getValue();
            value.setCoin(value.getCoin() + 3);
            entry.setValue(value);
            return null;
        });

        PlayerEntry p = (PlayerEntry) cache.get(1071010079177838592L);

        System.out.println("给ID为5的加3个coin，现在的coin为: " + p.getCoin());
    }

    @RequestMapping("/test/a")
    public void test() {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    PlayerEntry playerEntry = new PlayerEntry(IdCreator.nextId(PlayerEntry.class));
                    playerEntry.setName("aaa");
                    playerRepository.save(playerEntry);

//                    UserEntry userEntry = new UserEntry();
//                    userEntry.setUserName("bbb");
//                    userEntry.setPassWord("bbb");
//                    userRepository.save(userEntry);
                }
            }).start();
        }


    }

    @RequestMapping("/test/cache")
    public void cache() {


        IgniteCache<Long, BaseEntry> cache = CacheManager.getCache(CacheEnum.PlayerEntryCache);
        BaseEntry baseEntry = cache.get(1069906836339036160L);
        BaseEntry baseEntry2 = cache.get(1069907277709840384L);

        cache.clear(1069906836339036160L);
//cache.loadCache((aLong, baseEntry1) -> true,1069906836339036160L);


        System.out.println("======================");

        Iterable iterable = cache.localEntries(CachePeekMode.ALL);
        Iterator iterator = iterable.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            Cache.Entry<Long, PlayerEntry> playerEntryMap = (Cache.Entry<Long, PlayerEntry>) next;
            System.out.println(playerEntryMap.getKey());
            System.out.println(playerEntryMap.getValue().getId() + "_" + playerEntryMap.getValue().getName());
        }


    }
}
