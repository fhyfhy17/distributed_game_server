package com.controller.interceptor;

import com.controller.ControllerHandler;
import com.pojo.Message;

public interface HandlerInterceptor {
    default boolean preHandle(Message message, ControllerHandler handler) throws Exception {
        return true;
    }

    default void postHandle(Message message, ControllerHandler handler,com.google.protobuf.Message result) throws Exception {
    }
}
