package com.pojo;

import lombok.Data;

@Data
public class Player {
    private long uid;
    private String name;
    private int level = 1;
    private int gold;
}
