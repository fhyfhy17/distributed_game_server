package com.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConnectUser implements Serializable {


    private static final long serialVersionUID = 3591466965980622144L;
    private String sessionId;
    private String uid;
    private long playerId;
    private String gameId;
    private String gateId;
}
