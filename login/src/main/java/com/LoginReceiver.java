package com;

import com.enums.GroupEnum;
import com.handler.LoginMessageHandler;
import com.hanlder.MessageGroup;
import com.hanlder.MessageThreadHandler;
import com.pojo.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LoginReceiver extends BaseReceiver {

    private MessageGroup m;

    @PostConstruct
    public void startup() {
        m = new MessageGroup(GroupEnum.LOGIN_GROUP.name()) {
            @Override
            public MessageThreadHandler getMessageThreadHandler() {
                return new LoginMessageHandler();
            }
        };
        m.startup();
    }

    @Override
    public void onReceive(Message message) {
        m.messageReceived(message);
    }
}
