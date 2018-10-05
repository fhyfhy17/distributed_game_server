package com;

import com.handler.GameMessageHandler;
import com.hanlder.MessageGroup;
import com.hanlder.MessageThreadHandler;
import com.net.msg.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class GameReceiver extends BaseReceiver {
    private MessageGroup m;

    @PostConstruct
    public void startup() {
        m = new MessageGroup(Constant.GAME_GROUP) {
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
