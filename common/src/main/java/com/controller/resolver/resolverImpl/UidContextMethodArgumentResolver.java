package com.controller.resolver.resolverImpl;

import com.controller.UidContext;
import com.controller.resolver.ActionMethodArgumentResolver;
import com.controller.resolver.MethodParameter;
import com.pojo.Message;
import org.springframework.stereotype.Component;

@Component
public class UidContextMethodArgumentResolver implements ActionMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return UidContext.class.isAssignableFrom(parameter.getClassType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message message) throws Exception {
        return new UidContext(message.getUid(), message.getFrom());
    }
}
