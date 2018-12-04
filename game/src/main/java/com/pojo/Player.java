package com.pojo;

import com.entry.PlayerEntry;
import lombok.Data;

@Data
public class Player {

    private long playerId;
    private long uid;
    private PlayerEntry playerEntry;


}
