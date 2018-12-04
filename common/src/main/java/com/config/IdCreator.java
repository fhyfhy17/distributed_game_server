package com.config;

import cn.hutool.core.lang.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdCreator {

    @Bean
    public Snowflake snowflake() {
        return new Snowflake(1, 0, true);
    }
}
