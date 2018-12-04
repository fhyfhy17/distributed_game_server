package com.service;

import com.annotation.EventListener;
import com.config.CacheManager;
import com.dao.PlayerRepository;
import com.dao.UserRepository;
import com.entry.PlayerEntry;
import com.enums.CacheEnum;
import com.event.playerEvent.PlayerLoginEvent;
import com.google.common.eventbus.Subscribe;
import com.pojo.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@EventListener
@Slf4j
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OnlineService onlineService;

    @Subscribe
    public void login(PlayerLoginEvent playerLoginEvent) {
        long playerId = playerLoginEvent.getPlayerId();
        long uid = playerLoginEvent.getUid();
        Player player = loadPlayer(playerId);
        if (Objects.isNull(player)) {
            //TODO player不存在的处理
        } else {
            onlineService.putPlayer(uid, player);
            onlineService.putPlayer(player);
        }
    }

    private Player loadPlayer(long playerId) {
        PlayerEntry playerEntry = (PlayerEntry) CacheManager.getCache(CacheEnum.PlayerEntryCache).get(playerId);
        if (Objects.isNull(playerEntry)) {
            log.error("登录时不存在此player  playerId={}", playerId);
            return null;
        }
        Player player = new Player();
        player.setPlayerEntry(playerEntry);
        player.setPlayerId(playerEntry.getId());

        return player;

    }
}
