package com.config;

import com.pojo.ServerInfo;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteTransactions;
import org.apache.ignite.Ignition;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.apache.ignite.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.locks.Lock;

@Service
@Configuration
@EnableIgniteRepositories
public class IgniteConfig {

    @Autowired
    private ServerInfo serverInfo;

    @Bean
    public Ignite ignite() {
        Ignite ignite = Ignition.start("myignite.xml");
        return ignite;
    }
}
