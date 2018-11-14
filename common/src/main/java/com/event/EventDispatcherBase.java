package com.event;

import com.annotation.EventListener;
import com.google.common.eventbus.EventBus;
import com.util.SpringUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;


@Configuration
public class EventDispatcherBase {
    //TODO 异步事件还需要想

    private static EventBus eventBus;


    @PostConstruct
    public void init() {
        eventBus = new EventBus();

        Map<String, Object> eventListeners = SpringUtils.getBeansWithAnnotation(EventListener.class);
        if (MapUtils.isEmpty(eventListeners)) {
            return;
        }
        eventListeners.values().forEach(x -> eventBus.register(x));
    }


    @Bean(name = "eventBus")
    public EventBus getEventBus() {
        return eventBus;
    }


}