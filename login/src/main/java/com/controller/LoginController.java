package com.controller;


import com.entry.UserEntry;
import com.net.msg.LOGIN_MSG;
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
        String sessionId = req.getSessionId();

        LOGIN_MSG.STC_LOGIN.Builder builder = LOGIN_MSG.STC_LOGIN.newBuilder();
        UserEntry user = loginService.login(username, password);
        builder.setSessionId(sessionId);
        if (!Objects.isNull(user)) {
            builder.setUid(user.getId());
            builder.setSuc(true);
        } else {
            builder.setSuc(false);
        }

        return builder.build();

    }
}
