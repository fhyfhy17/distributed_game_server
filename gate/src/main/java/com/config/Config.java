package com.config;


import com.util.ContextUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Configuration
public class Config {

    @Bean
    public SocketAddress socketAddress() {
        return new InetSocketAddress(ContextUtil.tcpPort);
    }


}
