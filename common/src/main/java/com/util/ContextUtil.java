package com.util;

import com.controller.ControllerFactory;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ContextUtil {
    public static String id;


    public static int tcpPort;

    public static ControllerFactory controllerFactory;

    public static Ignite ignite;

    @Value("${server.serverId}")

    public void setId(String id) {
        ContextUtil.id = id;
    }

    @Value("${server.tcp.port}")
    public void setTcpPort(int port) {
        ContextUtil.tcpPort = port;
    }

    @Autowired
    public void setActionFactory(ControllerFactory controllerFactory) {
        ContextUtil.controllerFactory = controllerFactory;
    }

    @Autowired
    @Qualifier("myignite")
    public void setIgnite(Ignite ignite) {
        ContextUtil.ignite = ignite;
    }

}
