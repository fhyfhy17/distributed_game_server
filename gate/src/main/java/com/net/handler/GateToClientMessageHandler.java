package com.net.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import com.hanlder.MessageThreadHandler;
import com.net.ConnectManager;
import com.net.msg.LOGIN_MSG;
import com.net.msg.Options;
import com.pojo.Message;
import com.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class GateToClientMessageHandler extends MessageThreadHandler {

    @Override
    public void pulse() {
        while (!pulseQueues.isEmpty()) {
            try {
                Message message = pulseQueues.poll();

                dispatch(message);

            } catch (Exception e) {
                log.error("", e);
            }
        }
    }


    private void dispatch(Message message) throws InvalidProtocolBufferException {
        ConnectManager connectManager = SpringUtils.getBean(ConnectManager.class);

        //如果是登录返回消息
        if (message.getId() == LOGIN_MSG.STC_LOGIN.getDescriptor().getOptions().getExtension(Options.messageId)) {
            LOGIN_MSG.STC_LOGIN m = LOGIN_MSG.STC_LOGIN.parseFrom(message.getData());
            if (m.getSuc()) {

                connectManager.register(message.getUid(), m.getUid());
                connectManager.writeToClient(m.getUid(), message);
                return;
            }
        }
        String uid = message.getUid();
        connectManager.writeToClient(uid, message);
    }

}
