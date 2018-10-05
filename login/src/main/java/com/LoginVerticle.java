package com;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginVerticle extends BaseVerticle {

    @Autowired
    private LoginReceiver loginReceiver;

    @Override
    public BaseReceiver getReceiver() {
        return loginReceiver;
    }


}
