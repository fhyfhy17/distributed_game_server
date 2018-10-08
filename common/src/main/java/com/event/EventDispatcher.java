package com.event;

import com.annotation.EventListener;
import com.google.common.eventbus.EventBus;
import com.util.SpringUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class EventDispatcher {
    //TODO 异步事件还需要想

    private static EventBus eventBus;


    @PostConstruct
    public void init() {
        eventBus = new EventBus();

        Map<String, Object> eventListeners = SpringUtils.getBeansWithAnnotation(EventListener.class);
        if (MapUtils.isEmpty(eventListeners)) {
            return;
        }
        eventListeners.values().forEach(x -> register(x));
    }

    public static <T extends PlayerEventData> void dispatch(T eventData) {
        eventBus.post(eventData);
    }


    public static void register(Object listener) {
        eventBus.register(listener);
    }

    public static void unregister(Object listener) {
        eventBus.unregister(listener);
    }


}
