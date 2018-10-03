package com.util;

import com.action.ActionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ContextUtil {
    public static String id;


    public static int tcpPort;

    private static ActionFactory actionFactory;

    @Value("${server.serverId}")

    public void setId(String id) {
        ContextUtil.id = id;
    }

    @Value("${server.tcp.port}")
    public void setTcpPort(int port) {
        ContextUtil.tcpPort = port;
    }

    @Autowired
    public void setActionFactory(ActionFactory actionFactory) {
        ContextUtil.actionFactory = actionFactory;
    }

    public static ActionFactory getActionFactory() {
        return ContextUtil.actionFactory;
    }
}
