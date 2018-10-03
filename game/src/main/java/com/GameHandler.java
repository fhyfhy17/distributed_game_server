package com;

import com.hanlder.MessageGroup;
import com.net.msg.Message;
import com.util.SpringUtils;

public class GameHandler extends BaseHandler {
    private static GameHandler HANDLER = new GameHandler();

    @Override
    public void onReceive(Message message) {
        SpringUtils.getBeanByType(MessageGroup.class).messageReceived(message);
    }

    public static GameHandler getInstance() {
        return HANDLER;
    }
}
