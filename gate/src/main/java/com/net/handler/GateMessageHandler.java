package com.net.handler;

import com.Constant;
import com.enums.ServerTypeEnum;
import com.hanlder.MessageThreadHandler;
import com.hazelcast.util.StringUtil;
import com.manager.ConnectUserManger;
import com.manager.ServerInfoManager;
import com.manager.VertxMessageManager;
import com.net.msg.LOGIN_MSG;
import com.net.msg.Options;
import com.pojo.ConnectUser;
import com.pojo.Message;
import com.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GateMessageHandler extends MessageThreadHandler {

    @Override
    public void pulse() {
        while (!pulseQueues.isEmpty()) {
            try {
                Message message = pulseQueues.poll();
                final String uid = message.getUid();
                final int cmdId = message.getId();
                dispatch(message);

            } catch (Exception e) {
                log.error("", e);
            }
        }
    }


    private void dispatch(Message message) {
        message.setFrom(ContextUtil.id);
        //fixme 多的时候要倒着写啊
        if (message.getId() == LOGIN_MSG.CTS_LOGIN.getDescriptor().getOptions().getExtension(Options.messageId)) {
            String loginServerId = ServerInfoManager.hashChooseServer(message.getUid(), ServerTypeEnum.LOGIN);
            if (StringUtil.isNullOrEmpty(loginServerId)) {
                log.error("没有发现loginServer");
                return;
            }
            VertxMessageManager.sendMessage(loginServerId, message);
        } else if (message.getId() > Constant.GAME_PROTO_BEGIN) {
            ConnectUser connectUser = ConnectUserManger.getConnectUserCache().get(message.getUid());
            VertxMessageManager.sendMessage(connectUser.getGameId(), message);
        } else {

        }

    }

}
