package com.util;


import com.entry.BaseEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ContextUtil {
    public static String id;
    public static int type;
    public static int tcpPort;
    public static Snowflake snowflake;

    @Value("${server.serverId}")

    public void setId(String id) {
        ContextUtil.id = id;
    }

    @Value("${server.tcp.port}")
    public void setTcpPort(int port) {
        ContextUtil.tcpPort = port;
    }

    @Value("${server.type}")
    public void setType(int type) {
        ContextUtil.type = type;
    }

    public static int getIntId() {
        String substring = ContextUtil.id.substring(ContextUtil.id.indexOf("-") + 1);
        return Integer.parseInt(substring);
    }

    @Autowired
    public void setSnowflake(Snowflake snowflake) {
        ContextUtil.snowflake = snowflake;
    }

    public static long nextId(Class<? extends BaseEntry> clazz) {
        return ContextUtil.snowflake.nextId(clazz);
    }
}
