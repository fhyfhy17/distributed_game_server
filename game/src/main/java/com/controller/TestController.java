package com.controller;

import com.entry.PlayerEntry;
import com.event.EventDispatcher;
import com.event.playerEvent.TestEvent;
import com.google.protobuf.MessageLite;
import com.net.msg.LOGIN_MSG;
import com.pojo.Player;
import com.service.TestService;
import com.util.CountUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

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
        player.setUid(uidContext.getUid());
        player.setLevel(2);
        player.setName("王二");
        player.setGold(10);
        EventDispatcher.playerEventDispatch(new TestEvent(player, "测试"));
        CountUtil.count();
        List<PlayerEntry> 王二 = testService.findByName("王二");

        testService.findUserById(uidContext.getUid());
        if (CollectionUtils.isEmpty(王二)) {
            PlayerEntry playerEntry = new PlayerEntry();
            playerEntry.setName("王二");
            testService.save(playerEntry);
        }

        return null;
    }

    public MessageLite resetCount(LOGIN_MSG.LTGAME_RESET_COUNT req) {
        CountUtil.start();
        return null;
    }
}
