package com.event;

import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class EventDispatcher {

    private static EventBus eventBus;

    public static <T extends PlayerEventData> void dispatch(T eventData) {
        eventBus.post(eventData);
    }

    @Autowired
    @Qualifier(value = "eventBus")
    public void setEventBus(EventBus eventBus) {
        EventDispatcher.eventBus = eventBus;
    }

}
