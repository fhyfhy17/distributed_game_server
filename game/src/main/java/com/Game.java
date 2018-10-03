package com;


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

public class Game implements CommandLineRunner {


    public static void main(String[] args) {

        SpringApplication.run(Game.class, args);

        RunUtil.run(GameVerticle.class,
                new VertxOptions()
                        .setClusterManager(new HazelcastClusterManager())
                        .setClustered(true));

    }

    public void run(String... args) throws Exception {

    }
    @Autowired
    SpringUtils springUtils;
    @Autowired
    ContextUtil contextUtil;


}

