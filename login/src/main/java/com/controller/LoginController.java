package com.controller;

import com.net.msg.LOGIN_MSG;
import com.pojo.User;
import com.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class LoginController extends BaseController {
    @Autowired
    private LoginService loginService;

    public LOGIN_MSG.STC_LOGIN login(UidContext context, LOGIN_MSG.CTS_LOGIN req) {
        String username = req.getUsername();
        String password = req.getPassword();

        LOGIN_MSG.STC_LOGIN.Builder builder = LOGIN_MSG.STC_LOGIN.newBuilder();
        User user = loginService.login(username, password);

        if (!Objects.isNull(user)) {
//            ConnectUser connectUser = new ConnectUser();
//            connectUser.setUid(user.getUid());
//            connectUser.setGateId(context.getFrom());
//            //保存sessionId信息,登录成功之前,messeage里的uid就是sessionId
//            connectUser.setSessionId(context.getUid());
//            ConnectUserManger.saveConnectUser(connectUser);

            builder.setUid(user.getUid());
            builder.setSuc(true);
        } else {
            builder.setSuc(false);
        }
        return builder.build();

    }
}
