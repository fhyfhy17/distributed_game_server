package com;

import com.hanlder.MessageGroup;
import com.net.msg.Message;
import com.util.SpringUtils;


public class LoginHandler extends BaseHandler {
    private static LoginHandler HANDLER = new LoginHandler();

    @Override
    public void onReceive(Message message) {
        SpringUtils.getBeanByType(MessageGroup.class).messageReceived(message);
    }

    public static LoginHandler getInstance() {
        return HANDLER;
    }
}
