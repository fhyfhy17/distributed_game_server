package com.controller;

import com.event.EventDispatcher;
import com.event.playerEvent.PlayerLoginEvent;
import com.net.msg.LOGIN_MSG;
import com.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class LoginController extends BaseController {
    @Autowired
    private PlayerService playerService;


    public LOGIN_MSG.STC_GAME_LOGIN test(UidContext uidContext, LOGIN_MSG.CTS_GAME_LOGIN req) {
        EventDispatcher.playerEventDispatch(new PlayerLoginEvent(req.getPlayerId(), req.getUid()));
        return null;
    }

}