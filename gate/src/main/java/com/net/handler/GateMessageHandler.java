package com.net.handler;

import com.enums.ServerTypeEnum;
import com.hanlder.MessageThreadHandler;
import com.manager.ServerManager;
import com.pojo.Message;
import com.util.ContextUtil;
import com.util.RouteUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        int msgId = message.getId();
        switch (RouteUtil.route(msgId)) {
            case LOGIN:
                String loginServerId = ServerManager.hashChooseServer(message.getUid(), ServerTypeEnum.LOGIN);
                if (StringUtils.isEmpty(loginServerId)) {
                    log.error("没有发现loginServer");
                    return;
                }
                ServerManager.sendMessage(loginServerId, message);
                break;
            case GAME:
//                ConnectUser connectUser = ConnectUserManger.getConnectUser(message.getUid());
                ServerManager.sendMessage("game-1", message);
                break;
            case X:
                break;
            default:
                break;
        }
    }

}
