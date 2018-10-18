package com;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameVerticle extends BaseVerticle {

    @Autowired
    private GameReceiver gameReceiver;

//    @Override
//    public BaseReceiver getReceiver() {
//        return gameReceiver;
//    }


}
