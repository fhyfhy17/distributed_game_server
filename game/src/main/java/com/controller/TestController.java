package com.controller;

import com.config.CacheManager;
import com.entry.BaseEntry;
import com.entry.PlayerEntry;
import com.enums.CacheEnum;
import com.event.EventDispatcher;
import com.event.playerEvent.TestEvent;
import com.google.protobuf.MessageLite;
import com.net.msg.LOGIN_MSG;
import com.pojo.Player;
import com.service.TestService;
import com.util.CountUtil;
import com.util.IdCreator;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.IgniteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class TestController extends BaseController {
    @Autowired
    private TestService testService;


    public LOGIN_MSG.STC_TEST test(UidContext uidContext, LOGIN_MSG.CTS_TEST req) {
//        log.info("test收到word = {}", req.getWord());
        LOGIN_MSG.STC_TEST.Builder builder = LOGIN_MSG.STC_TEST.newBuilder();
        builder.setWord(req.getWord());

        //发一个事件
        Player player = new Player();
//        player.setUid(uidContext.getUid());
//        player.setLevel(2);
//        player.setName("王二");
//        player.setGold(10);

        CountUtil.count();
//        List<PlayerEntry> 王二 = testService.findByName("王二");
//        testService.findUserById(uidContext.getUid());
//        if (CollectionUtils.isEmpty(王二)) {
        PlayerEntry playerEntry = new PlayerEntry(IdCreator.nextId(PlayerEntry.class));
        playerEntry.setName("王=四");

        IgniteCache<Long, BaseEntry> cache = CacheManager.getCache(CacheEnum.PlayerEntryCache);
        cache.put(playerEntry.getId(), playerEntry);

//        }
        EventDispatcher.playerEventDispatch(new TestEvent(player.getPlayerId(), "mykey"));
        return null;
    }

    public MessageLite resetCount(LOGIN_MSG.LTGAME_RESET_COUNT req) {
        CountUtil.start();
        return null;
    }
}
