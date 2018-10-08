package com.controller;

import com.net.msg.LOGIN_MSG;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class TestController extends BaseController {

    public LOGIN_MSG.STC_TEST test(LOGIN_MSG.CTS_TEST req) {
        log.info("test收到word = {}", req.getWord());
        LOGIN_MSG.STC_TEST.Builder builder = LOGIN_MSG.STC_TEST.newBuilder();
        builder.setWord(req.getWord());
        return builder.build();
    }
}
