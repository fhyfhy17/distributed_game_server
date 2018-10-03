package com;


import com.enums.ServerTypeEnum;
import com.pojo.ServerInfo;
import com.util.ContextUtil;

public class LoginVerticle extends BaseVerticle {

    @Override
    public ServerInfo getServerInfo() {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setServerType(ServerTypeEnum.LOGIN);
        serverInfo.setServerId(ContextUtil.id);
        return serverInfo;
    }

    @Override
    public BaseHandler getHandler() {
        return LoginHandler.getInstance();
    }


}
