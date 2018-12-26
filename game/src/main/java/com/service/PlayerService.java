package com.service;

import com.alibaba.fastjson.JSONObject;
import com.annotation.EventListener;
import com.annotation.ServiceLog;
import com.aop.ServiceLogAspect;
import com.config.CacheManager;
import com.dao.PlayerRepository;
import com.dao.UserRepository;
import com.entry.PlayerEntry;
import com.entry.UserEntry;
import com.enums.CacheEnum;
import com.event.playerEvent.PlayerLoginEvent;
import com.exception.exceptionNeedSendToClient.NoPlayerWhenLoginException;
import com.google.common.eventbus.Subscribe;
import com.net.msg.LOGIN_MSG;
import com.pojo.Player;
import com.util.IdCreator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
    @ServiceLog
    public void login(PlayerLoginEvent playerLoginEvent) throws NoPlayerWhenLoginException {
        long playerId = playerLoginEvent.getPlayerId();
        long uid = playerLoginEvent.getUid();
        LOGIN_MSG.STC_GAME_LOGIN_PLAYER.Builder builder = playerLoginEvent.getBuilder();
        Player player = loadPlayer(playerId);
        onlineService.putPlayer(uid, player);
        onlineService.putPlayer(player);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("player", player);
        ServiceLogAspect.THREAD_LOCAL.set(jsonObject);
        builder.setPlayerInfo(buildPlayerInfo(player.getPlayerEntry()));
    }

    private Player loadPlayer(long playerId) throws NoPlayerWhenLoginException {
        AtomicInteger a = new AtomicInteger();
        a.addAndGet(2);


        PlayerEntry playerEntry = (PlayerEntry) CacheManager.getCache(CacheEnum.PlayerEntryCache).get(playerId);
        if (Objects.isNull(playerEntry)) {
            throw new NoPlayerWhenLoginException();
        }
        Player player = new Player();
        player.setPlayerEntry(playerEntry);
        player.setPlayerId(playerEntry.getId());
        player.setUid(playerEntry.getUid());

        return player;

    }

    public void playerList(long uid, LOGIN_MSG.STC_PLAYER_LIST.Builder builder) {
        Optional<UserEntry> user = userRepository.findById(uid);
        UserEntry userEntry = user.get();
        List<Long> playerIds = userEntry.getPlayerIds();
        if (CollectionUtils.isEmpty(playerIds)) {
            // 创建一个角色并存储
            long index = IdCreator.nextId(PlayerEntry.class);
            PlayerEntry playerEntry = new PlayerEntry(index);
            playerEntry.setName("游客" + index);
            playerEntry.setUid(uid);
            CacheManager.getCache(CacheEnum.PlayerEntryCache).put(index, playerEntry);
            // 存储到角色列表
            userEntry.getPlayerIds().add(index);
            userRepository.save(userEntry);
            playerIds = userEntry.getPlayerIds();
        }
        for (Long playerId : playerIds) {
            PlayerEntry playerEntry = (PlayerEntry) CacheManager.getCache(CacheEnum.PlayerEntryCache).get(playerId);

            builder.addPlayers(buildPlayerInfo(playerEntry));
        }

    }

    public LOGIN_MSG.PLAYER_INFO.Builder buildPlayerInfo(PlayerEntry playerEntry) {
        LOGIN_MSG.PLAYER_INFO.Builder playerBuilder = LOGIN_MSG.PLAYER_INFO.newBuilder();
        playerBuilder.setUid(playerEntry.getUid());
        playerBuilder.setPlayerId(playerEntry.getId());
        playerBuilder.setName(playerEntry.getName());
        playerBuilder.setLevel(playerEntry.getLevel());
        playerBuilder.setCoin(playerEntry.getCoin());
        return playerBuilder;
    }
}
