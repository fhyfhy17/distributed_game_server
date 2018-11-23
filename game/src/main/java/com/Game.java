package com;


import com.controller.ControllerFactory;
import com.util.ContextUtil;
import com.util.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
public class Game implements CommandLineRunner {
    @Autowired
    SpringUtils springUtils;
    @Autowired
    ContextUtil contextUtil;

    public static void main(String[] args) {
        SpringApplication.run(Game.class, args);
    }

    public void run(String... args) throws Exception {

    }


    @EventListener
    void afterSrpingBoot(ApplicationReadyEvent event) throws Exception {
        ControllerFactory.init();
    }
}

