package com.controller;

import com.entry.PlayerEntry;
import com.google.protobuf.MessageLite;
import com.net.msg.LOGIN_MSG;
import com.service.TestService;
import com.util.CountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

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
//        Player player  = new Player();
//        player.setUid(uidContext.getUid());
//        player.setLevel(2);
//        player.setName("王二");
//        player.setGold(10);
//        EventDispatcher.dispatch(new TestEvent(player,"测试"));
        CountUtil.count();

        PlayerEntry playerEntry = new PlayerEntry();
        playerEntry.setName("张老在");
        testService.save(playerEntry);

        List<PlayerEntry> entrys = testService.findByName("张老在");
        for (PlayerEntry entry : entrys) {
            System.out.println(entry.getName() + "" + entry.getId());
        }
        List<PlayerEntry> all = testService.findAll();
        for (PlayerEntry entry1 : all) {
            System.out.println(entry1.getName() + "" + entry1.getId());
        }

        Optional<PlayerEntry> byId = testService.findById(playerEntry.getId());
        System.out.println("==================================================");
        System.out.println(byId.map(playerEntry1 -> playerEntry1.getId() + "--" + playerEntry1.getName()).orElse("123"));


        return null;
    }

    public MessageLite resetCount(LOGIN_MSG.LTGAME_RESET_COUNT req) {
        CountUtil.start();
        return null;
    }
}
