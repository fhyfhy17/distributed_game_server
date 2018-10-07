package com.controller.resolver;

import com.pojo.Message;

//TODO 还要再支持一个Player参数实现！！！！
//TODO 再做个拦截器~~~ 实现 白名单黑名单功能好不
public interface ActionMethodArgumentResolver {

    boolean supportsParameter(MethodParameter parameter);

    Object resolveArgument(MethodParameter parameter, Message message) throws Exception;
}
