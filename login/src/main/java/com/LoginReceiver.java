package com;

import com.hanlder.MessageGroup;
import com.net.msg.Message;
import com.util.SpringUtils;
import org.springframework.stereotype.Component;

@Component
public class LoginReceiver extends BaseReceiver {
    @Override
    public void onReceive(Message message) {
        SpringUtils.getBean(MessageGroup.class).messageReceived(message);
    }
}
