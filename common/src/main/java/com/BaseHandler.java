package com;

import com.net.msg.Message;

/**
 * handler应当是单例模式
 */
public abstract class BaseHandler {

    public abstract void onReceive(Message message);


}
