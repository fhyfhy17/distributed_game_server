package com.controller;

import com.net.msg.LOGIN_MSG;
import org.springframework.stereotype.Controller;

@Controller
public class TestController extends BaseController {

    public LOGIN_MSG.STC_TEST test(LOGIN_MSG.CTS_TEST req) {
        System.out.println(req.getWord());
        LOGIN_MSG.STC_TEST.Builder builder = LOGIN_MSG.STC_TEST.newBuilder();
        builder.setWord(req.getWord());
        return builder.build();
    }
}
