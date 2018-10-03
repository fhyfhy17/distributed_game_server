package com;

import com.enums.ServerTypeEnum;
import com.pojo.ServerInfo;
import com.util.ContextUtil;

public class GateVerticle extends BaseVerticle {

    @Override
    public BaseHandler getHandler() {
        return GateHandler.getInstance();
    }

    @Override
    public ServerInfo getServerInfo() {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setServerType(ServerTypeEnum.GATE);
        serverInfo.setServerId(ContextUtil.id);
        return serverInfo;
    }


}
