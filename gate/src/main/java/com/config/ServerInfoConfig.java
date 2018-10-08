package com.config;

import com.enums.ServerTypeEnum;
import com.pojo.ServerInfo;
import com.util.ContextUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Configuration
public class ServerInfoConfig {

    @Bean
    public ServerInfo serverInfo() {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setServerType(ServerTypeEnum.GATE);
        serverInfo.setServerId(ContextUtil.id);
        return serverInfo;
    }

    @Bean
    public SocketAddress socketAddress() {
        return new InetSocketAddress(ContextUtil.tcpPort);
    }
}
