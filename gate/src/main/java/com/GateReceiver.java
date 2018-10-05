package com;

import com.net.handler.GateToClientGroupHandler;
import com.net.msg.Message;
import com.util.SpringUtils;
import org.springframework.stereotype.Component;

@Component
public class GateReceiver extends BaseReceiver {
    @Override
    public void onReceive(Message message) {
        //这收到其它服务器返回消息直接刷到前端了
        SpringUtils.getBean(GateToClientGroupHandler.class).messageReceived(message);
    }
}
