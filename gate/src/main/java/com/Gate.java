package com;

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

    private  static NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(Gate.class, args);
        nettyServer.init();
    }

    public void run(String... args) throws Exception {

    }


    @Autowired
    public void setNettyServer(NettyServer nettyServer) {
        Gate.nettyServer = nettyServer;
    }

}
