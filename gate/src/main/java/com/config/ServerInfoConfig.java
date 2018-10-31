package com.config;

import com.enums.ServerTypeEnum;
import com.pojo.ServerInfo;
import com.util.ContextUtil;
import com.util.IpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Configuration
public class ServerInfoConfig {
    @Value("${server.netty.port}")
    private int nettyPort;
    @Bean
    public ServerInfo serverInfo() {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setServerType(ServerTypeEnum.GATE);
        serverInfo.setServerId(ContextUtil.id);
        serverInfo.setIp(IpUtil.getHostIp());
        serverInfo.setPort(ContextUtil.tcpPort);
        return serverInfo;
    }

    @Bean
    public SocketAddress socketAddress() {
        return new InetSocketAddress(nettyPort);
    }
}
