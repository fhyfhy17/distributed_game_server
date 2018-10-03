package com;

import com.net.handler.GateToClientGroupHandler;
import com.net.msg.Message;
import com.util.SpringUtils;


public class GateHandler extends BaseHandler {
    private static GateHandler HANDLER = new GateHandler();

    @Override
    public void onReceive(Message message) {
        //TODO 这收到其它服务器返回消息直接刷到前端了
        SpringUtils.getBeanByType(GateToClientGroupHandler.class).messageReceived(message);
    }

    public static GateHandler getInstance() {
        return HANDLER;
    }

}
