package com.pojo;

import com.enums.ServerTypeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class ServerInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -533104123L;
    private ServerTypeEnum serverType;
    private String serverId;
}
