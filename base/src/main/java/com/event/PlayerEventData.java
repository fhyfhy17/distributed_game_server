package com.event;

import com.pojo.Player;
import lombok.Data;

import java.util.Date;

@Data
public class PlayerEventData {

    public PlayerEventData(Player player) {
        this.player = player;
    }

    private Player player;

    private Date time = new Date();
}
