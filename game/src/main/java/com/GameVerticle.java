package com;


import com.enums.ServerTypeEnum;
import com.pojo.ServerInfo;
import com.util.ContextUtil;
import org.springframework.stereotype.Component;

@Component
public class GameVerticle extends BaseVerticle {

    @Override
    public ServerInfo getServerInfo() {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setServerType(ServerTypeEnum.GAME);
        serverInfo.setServerId(ContextUtil.id);
        return serverInfo;
    }

    @Override
    public BaseHandler getHandler() {
        return GameHandler.getInstance();
    }


}
