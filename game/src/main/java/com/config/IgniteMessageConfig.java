package com.config;

import com.util.SpringUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@DependsOn("springUtils")
public class IgniteMessageConfig {
    @Bean("im")
    public IgniteMessaging getIgniteMessaging() {
        Ignite ignite = SpringUtils.getBean(Ignite.class);
        return ignite.message(ignite.cluster().forRemotes());
    }
}
