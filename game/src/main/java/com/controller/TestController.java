package com.controller;

import com.dao.PlayerRepository;
import com.entry.PlayerEntry;
import com.google.protobuf.MessageLite;
import com.net.msg.LOGIN_MSG;
import com.util.CountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class TestController extends BaseController {
    @Autowired
    private PlayerRepository playerRepository;
    public LOGIN_MSG.STC_TEST test(UidContext uidContext, LOGIN_MSG.CTS_TEST req) {
//        log.info("test收到word = {}", req.getWord());
        LOGIN_MSG.STC_TEST.Builder builder = LOGIN_MSG.STC_TEST.newBuilder();
        builder.setWord(req.getWord());

        //发一个事件
//        Player player  = new Player();
//        player.setUid(uidContext.getUid());
//        player.setLevel(2);
//        player.setName("王二");
//        player.setGold(10);
//        EventDispatcher.dispatch(new TestEvent(player,"测试"));
        CountUtil.count();

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setName("张老三");
        playerRepository.save(playerEntry);
        playerEntry.setName("王在");
        playerRepository.save(playerEntry);
        return null;
    }

    public MessageLite resetCount(LOGIN_MSG.LTGAME_RESET_COUNT req) {
        CountUtil.start();
        return null;
    }
}
