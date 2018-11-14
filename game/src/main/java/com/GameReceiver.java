package com;

import com.enums.GroupEnum;
import com.handler.GameMessageHandler;
import com.handler.MessageGroup;
import com.handler.MessageThreadHandler;
import com.pojo.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class GameReceiver extends BaseReceiver {
    private MessageGroup m;

    @PostConstruct
    public void startup() {
        m = new MessageGroup(GroupEnum.GAME_GROUP.name()) {
            @Override
            public MessageThreadHandler getMessageThreadHandler() {
                return new GameMessageHandler();
            }
        };
        m.startup();
    }

    @Override
    public void onReceive(Message message) {
        m.messageReceived(message);
    }

}
