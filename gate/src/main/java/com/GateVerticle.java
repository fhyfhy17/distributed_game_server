package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GateVerticle extends BaseVerticle {
    @Autowired
    private GateReceiver gateReceiver;

//    @Override
//    public BaseReceiver getReceiver() {
//        return gateReceiver;
//    }
}
