package com.controller;

import com.config.CacheManager;
import com.dao.PlayerRepository;
import com.dao.UserRepository;
import com.entry.BaseEntry;
import com.entry.PlayerEntry;
import com.entry.UnionEntry;
import com.enums.CacheEnum;
import com.lock.zk.ZkDistributedLock;
import com.manager.ServerInfoManager;
import com.manager.VertxMessageManager;
import com.mongoListener.SaveEventListener;
import com.net.msg.LOGIN_MSG;
import com.net.msg.Options;
import com.node.RemoteNode;
import com.pojo.Message;
import com.service.UnionService;
import com.util.ContextUtil;
import com.util.IdCreator;
import com.util.SerializeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteMessaging;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.cache.Cache;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

//import com.config.RedissonConfig;

@RestController
@Slf4j
public class WebTestEnter {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SaveEventListener saveEventListener;
    @Autowired
    private Ignite ignite;
    @Autowired
    @Qualifier(value = "im")
    private IgniteMessaging igniteMessaging;
    @Autowired
    private UnionService unionService;
//    @Autowired
//    RedissonConfig redissonConfig;


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

    @RequestMapping("/test/addUnion")
    public void addUnion() {
        UnionEntry u = new UnionEntry(IdCreator.nextId(UnionEntry.class));
        IgniteCache<Long, BaseEntry> cache = CacheManager.getCache(CacheEnum.UnionEntryCache);
        cache.put(u.getId(), u);
    }

    @RequestMapping("/test/createPlayer")
    public void createPlayer() {
        PlayerEntry playerEntry = new PlayerEntry(IdCreator.nextId(PlayerEntry.class));
        IgniteCache<Long, BaseEntry> cache = CacheManager.getCache(CacheEnum.PlayerEntryCache);
        cache.put(playerEntry.getId(), playerEntry);
//        1077939648774410240
    }

    AtomicInteger a = new AtomicInteger(0);
    StopWatch s = new StopWatch();
    ZkDistributedLock lock = new ZkDistributedLock(ContextUtil.zkIpPort, 1000, "textLock");

    @RequestMapping("/test/playerAddUnion")
    public void playerAddUnion() {
        unionService.examinePlayer(1078490924780228608L, 1071010079177838592L);
//        1077939648774410240
//        1077941486252855296
    }

    @RequestMapping("/test/testZk")
    public void testZk() {
        s.reset();
        s.start();
        for (int i = 0; i < 1000; i++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(() -> {
                lock.lock("a");
                try {
                    System.out.println(Thread.currentThread().getId() + " 获得了锁");
                    int i1 = this.a.incrementAndGet();
                    if (i1 != 0 && i1 % 1000 == 0) {
                        s.stop();
                        System.out.println("1000个 锁已经全部完事了，共用了：" + s.getTime());
                    }

                } finally {
                    lock.unlock();
                }

//                RedissonClient lock = redissonConfig.getClient();
//
//
////                RedissonRedLock redLock = new RedissonRedLock(lock.getLock("lock1"), lock.getLock("lock2"), lock.getLock("lock3"));
//
//                RLock redLock = lock.getLock("a");
//                try {
//                    boolean res = false;
//                    try {
//                        res = redLock.tryLock(30, 3, TimeUnit.SECONDS);
//                        if (res) {
//                            System.out.println(Thread.currentThread().getId() + " 获得了锁");
//                            int i1 = this.a.incrementAndGet();
//                            if (i1!=0&&i1 % 1000==0) {
//                                s.stop();
//                                System.out.println("1000个 锁已经全部完事了，共用了："+s.getTime());
//                            }
//                        }
//
//                    } catch (InterruptedException e) {
//                        log.error("", e);
//                    }
//
//                } finally {
//                    redLock.unlock();
//                }

            }
            ).start();
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

    @RequestMapping("/test/igniteMessage")
    public void testIgniteMessage() {
        for (int i = 0; i < 1000000; i++) {
            igniteMessaging.sendOrdered("login-1", "abcdefghigklmnopqrstuvwxyz", 10000);
        }

    }

    @RequestMapping("/test/vertxMessage")
    public void testVertxMessage() {
        Random r = new Random();
        Message message = new Message();
        LOGIN_MSG.TEST_TIME.Builder builder = LOGIN_MSG.TEST_TIME.newBuilder();
        builder.setMsg("abcdefghigklmnopqrstuvwxyz");

        message.setData(builder.build().toByteArray());
        message.setId(LOGIN_MSG.TEST_TIME.getDescriptor().getOptions().getExtension(Options.messageId));
        message.setFrom(ContextUtil.id);


        for (int i = 0; i < 1000000; i++) {
            message.setUid(1);
            VertxMessageManager.sendMessage("login-1", message);
        }

    }

    @RequestMapping("/test/zeromqMessage")
    public void testZeromqMessage() {
        Message message = new Message();
        LOGIN_MSG.TEST_TIME.Builder builder = LOGIN_MSG.TEST_TIME.newBuilder();
        builder.setMsg("abcdefghigklmnopqrstuvwxyz");

        message.setData(builder.build().toByteArray());
        message.setId(LOGIN_MSG.TEST_TIME.getDescriptor().getOptions().getExtension(Options.messageId));
        message.setFrom(ContextUtil.id);
        message.setUid(1);
        for (int i = 0; i < 1000000; i++) {
            RemoteNode remoteNode = ServerInfoManager.getRemoteNode("login-1");
            remoteNode.sendReqMsg(SerializeUtil.mts(message));
        }

    }
}
