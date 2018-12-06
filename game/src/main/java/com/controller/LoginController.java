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


    public LOGIN_MSG.STC_PLAYER_LIST playerList(UidContext uidContext, LOGIN_MSG.CTS_PLAYER_LIST req) {
        LOGIN_MSG.STC_PLAYER_LIST.Builder builder = LOGIN_MSG.STC_PLAYER_LIST.newBuilder();

        playerService.playerList(uidContext.getUid(), builder);

        return builder.build();
    }

    public LOGIN_MSG.STC_GAME_LOGIN_PLAYER gameLogin(UidContext uidContext, LOGIN_MSG.CTS_GAME_LOGIN_PLAYER req) {
        LOGIN_MSG.STC_GAME_LOGIN_PLAYER.Builder builder = LOGIN_MSG.STC_GAME_LOGIN_PLAYER.newBuilder();
        EventDispatcher.playerEventDispatch(new PlayerLoginEvent(req.getPlayerId(), uidContext.getUid(), builder));
        return builder.build();
    }

    public LOGIN_MSG.STC_PlayerInfo getPlayerInfo(UidContext uidContext, Player player, LOGIN_MSG.CTS_PlayerInfo req) {
        LOGIN_MSG.STC_PlayerInfo.Builder builder = LOGIN_MSG.STC_PlayerInfo.newBuilder();

        builder.setPlayerInfo(playerService.buildPlayerInfo(player.getPlayerEntry()));
        return builder.build();
    }


}
