package com.controller;

import com.event.EventDispatcher;
import com.event.playerEvent.PlayerLoginEvent;
import com.net.msg.LOGIN_MSG;
import com.pojo.Player;
import com.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class LoginController extends BaseController {
    @Autowired
    private PlayerService playerService;


    public LOGIN_MSG.STC_GAME_LOGIN login(UidContext uidContext, LOGIN_MSG.CTS_GAME_LOGIN req) {
        EventDispatcher.playerEventDispatch(new PlayerLoginEvent(req.getPlayerId(), req.getUid()));
        return null;
    }

    public LOGIN_MSG.STC_PlayerInfo getPlayerInfo(UidContext uidContext, Player player, LOGIN_MSG.CTS_PlayerInfo req) {
        LOGIN_MSG.STC_PlayerInfo.Builder builder = LOGIN_MSG.STC_PlayerInfo.newBuilder();

        builder.setPlayerId(player.getPlayerId());
        builder.setName(player.getPlayerEntry().getName());
        builder.setUid(uidContext.getUid());
        builder.setLevel(player.getPlayerEntry().getLevel());
        return builder.build();
    }


}
