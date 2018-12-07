package com.config;

import com.enums.ServerTypeEnum;
import com.pojo.ServerInfo;
import com.util.ContextUtil;
import com.util.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerInfoConfig {

    @Bean
    public ServerInfo serverInfo() {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setServerType(ServerTypeEnum.values()[ContextUtil.type]);
        serverInfo.setServerId(ContextUtil.id);
        serverInfo.setType(ContextUtil.type);
        return serverInfo;
    }

    @Bean
    public Snowflake snowflake() {
        return new Snowflake(ContextUtil.getIntId(), ContextUtil.type);
    }


}