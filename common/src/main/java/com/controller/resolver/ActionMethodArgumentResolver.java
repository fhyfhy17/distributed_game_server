package com.controller.resolver;

import com.pojo.Message;

//TODO Player参数实现
//TODO 拦截器可以实现 白名单黑名单功能，类似于多少级以下的不能访问某些协议之类的
public interface ActionMethodArgumentResolver {

    boolean supportsParameter(MethodParameter parameter);

    Object resolveArgument(MethodParameter parameter, Message message) throws Exception;
}
