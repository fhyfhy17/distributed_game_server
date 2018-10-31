package com.controller;

import com.manager.VertxMessageManager;
import com.net.msg.LOGIN_MSG;
import com.net.msg.Options;
import com.pojo.Message;
import com.pojo.User;
import com.service.LoginService;
import com.util.ContextUtil;
import com.util.SerializeUtil;
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
            builder.setUid(user.getUid());
            builder.setSuc(true);
        } else {
            builder.setSuc(false);
        }

        LOGIN_MSG.LTGAME_RESET_COUNT.Builder builder1 = LOGIN_MSG.LTGAME_RESET_COUNT.newBuilder();
        LOGIN_MSG.LTGAME_RESET_COUNT build = builder1.build();
        Message mLtG = new Message();
        mLtG.setId(build.getDescriptorForType().getOptions().getExtension(Options.messageId));
        mLtG.setUid(context.getUid());
        mLtG.setFrom(ContextUtil.id);
        mLtG.setData(build.toByteArray());
        VertxMessageManager.sendMessage("game-1", mLtG);
        return builder.build();

    }
}
