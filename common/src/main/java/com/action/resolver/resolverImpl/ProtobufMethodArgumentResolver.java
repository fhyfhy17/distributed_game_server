package com.action.resolver.resolverImpl;

import com.action.resolver.ActionMethodArgumentResolver;
import com.action.resolver.MethodParameter;
import com.pojo.Message;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;

@Component
public class ProtobufMethodArgumentResolver implements ActionMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (com.google.protobuf.Message.class.isAssignableFrom(parameter.getClassType())) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message message) throws Exception {
        Class<? extends com.google.protobuf.Message> messageProto = (Class<? extends com.google.protobuf.Message>) parameter.getClassType();
        Constructor<? extends com.google.protobuf.Message> cons = messageProto.getDeclaredConstructor();
        cons.setAccessible(true);
        return cons.newInstance().getParserForType().parseFrom(message.getData());

    }
}
