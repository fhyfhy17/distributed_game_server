package com;

import com.hanlder.MessageGroup;
import com.hanlder.MessageThreadHandler;
import com.net.handler.GateToClientMessageHandler;
import com.net.msg.Message;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class GateReceiver extends BaseReceiver {
    private MessageGroup m;

    @PostConstruct
    public void startup() {
        m = new MessageGroup(Constant.GATE_TO_CLIENT_GROUP) {
            @Override
            public MessageThreadHandler getMessageThreadHandler() {
                return new GateToClientMessageHandler();
            }
        };
        m.startup();
    }

    @Override
    public void onReceive(Message message) {
        //这收到其它服务器返回消息直接刷到前端了
        m.messageReceived(message);
    }
}
