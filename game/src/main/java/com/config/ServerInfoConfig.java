package com.config;

import com.enums.ServerTypeEnum;
import com.pojo.ServerInfo;
import com.util.ContextUtil;
import com.util.IpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerInfoConfig {

    @Bean
    public ServerInfo serverInfo() {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setServerType(ServerTypeEnum.GAME);
        serverInfo.setServerId(ContextUtil.id);
        serverInfo.setIp(IpUtil.getHostIp());
        serverInfo.setPort(ContextUtil.tcpPort);
        return serverInfo;
    }
}
