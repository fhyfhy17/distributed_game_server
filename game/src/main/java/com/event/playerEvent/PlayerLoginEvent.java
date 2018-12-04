package com.event.playerEvent;

import com.event.PlayerEventData;
import lombok.Data;

@Data
public class PlayerLoginEvent extends PlayerEventData {
    private long uid;

    public PlayerLoginEvent(long playerId, long uid) {
        super(playerId);
        this.uid = uid;
    }
}
