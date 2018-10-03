package com;

import com.net.NettyServer;
import com.util.ContextUtil;
import com.util.RunUtil;
import com.util.SpringUtils;
import io.vertx.core.VertxOptions;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Gate implements CommandLineRunner {

    private static NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(Gate.class, args);
        RunUtil.run(GateVerticle.class,
                new VertxOptions()
                        .setClusterManager(new HazelcastClusterManager())
                        .setClustered(true));


        //启动netty
        nettyServer.init();
    }

    public void run(String... args) throws Exception {

    }


    private static void startMessageHandler() {

    }
    @Autowired
    SpringUtils springUtils;
    @Autowired
    ContextUtil contextUtil;


    @Autowired
    public void setNettyServer(NettyServer nettyServer) {
        Gate.nettyServer = nettyServer;
    }

}
