package com.net.handler;

import com.hanlder.MessageGroup;
import com.hanlder.MessageThreadHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class GateToClientGroupHandler extends MessageGroup {
    @Override
    @PostConstruct
    public void startup() {
        super.startup();
    }

    @Override
    public void initHandlers(int count, String name) {
        for (int i = 0; i < count; i++) {
            MessageThreadHandler handler = new GateToClientMessageHandler();
            handler.setName(name + i);
            handler.startup();
            hanlderList.add(handler);
        }
    }
}
