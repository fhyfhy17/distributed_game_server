package com.controller.resolver;

import com.pojo.Message;

//TODO Player参数实现
public interface ActionMethodArgumentResolver {

    boolean supportsParameter(MethodParameter parameter);

    Object resolveArgument(MethodParameter parameter, Message message) throws Exception;
}
