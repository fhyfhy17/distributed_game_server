package com;


import com.util.ContextUtil;
import com.util.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Login implements CommandLineRunner {
    @Autowired
    private SpringUtils springUtils;
    @Autowired
    private ContextUtil contextUtil;
    @Autowired
    private BaseVerticle baseVerticle;

    public static void main(String[] args) {
        SpringApplication.run(Login.class, args);
    }

    public void run(String... args) throws Exception {

    }


    @EventListener
    void afterSrpingBoot(ApplicationReadyEvent event) throws Exception {
        //启动 vertx
        baseVerticle.init();
    }
}

