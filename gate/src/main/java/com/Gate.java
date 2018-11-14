package com;

import com.controller.ControllerFactory;
import com.net.NettyServer;
import com.util.ContextUtil;
import com.util.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Gate implements CommandLineRunner {
    @Autowired
    SpringUtils springUtils;
    @Autowired
    ContextUtil contextUtil;
    @Autowired
    NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(Gate.class, args);
    }

    public void run(String... args) throws Exception {

    }


    @EventListener
    void afterSrpingBoot(ApplicationReadyEvent event) throws Exception {
        ControllerFactory.init();
        //启动 netty
        nettyServer.init();

    }
}
