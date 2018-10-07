package com.controller.resolver;

import com.pojo.Message;
import com.util.SpringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class ResolverManager {

    private List<ActionMethodArgumentResolver> actionMethodArgumentResolvers;

    @PostConstruct
    public void init() {
        actionMethodArgumentResolvers = new ArrayList<>(
                SpringUtils.getBeansOfType(ActionMethodArgumentResolver.class).values());
    }

    public Object resolve(MethodParameter parameter, Message message) throws Exception {
        for (ActionMethodArgumentResolver actionMethodArgumentResolver : actionMethodArgumentResolvers) {
            if (actionMethodArgumentResolver.supportsParameter(parameter)) {
                return actionMethodArgumentResolver.resolveArgument(parameter, message);
            }
        }
        return null;
    }
}