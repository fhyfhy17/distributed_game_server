package com.net.handler;

import com.*;
import com.enums.ServerTypeEnum;
import com.hanlder.MessageThreadHandler;
import com.hazelcast.util.StringUtil;
import com.manager.ConnectUserManger;
import com.manager.ServerInfoManager;
import com.manager.VertxMessageManager;
import com.net.msg.LOGIN_MSG;
import com.net.msg.Message;
import com.net.msg.Options;
import com.pojo.ConnectUser;
import com.util.ContextUtil;
import io.vertx.core.logging.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GateMessageHandler extends MessageThreadHandler {
    final static io.vertx.core.logging.Logger log = LoggerFactory.getLogger(MessageThreadHandler.class);

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


    private void dispatch(Message message)  {
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
            //TODO 这样每次发消息，都有可能有一个内网查询map 的过程。。。以后试着改善一下吧
            ConnectUser connectUser = ConnectUserManger.getConnectUser(message.getUid());

            VertxMessageManager.sendMessage(connectUser.getGameId(), message);
        } else {

        }

    }

}
