package com.controller;

import com.annotation.Interceptor;
import com.controller.interceptor.HandlerExecutionChain;
import com.controller.interceptor.HandlerInterceptor;
import com.google.common.collect.Maps;
import com.google.protobuf.Message;
import com.net.msg.Options;
import com.util.ReflectionUtil;
import com.util.SpringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ControllerFactory {

    private Map<Integer, ControllerHandler> controllerMap = Maps.newHashMap();
    private HandlerExecutionChain handlerExecutionChain;


    @PostConstruct
    private void init() {
        Map<String, BaseController> allControllers = SpringUtils.getBeansOfType(BaseController.class);
        allControllers.values().forEach(controller -> {
            Method[] declaredMethods = controller.getClass().getDeclaredMethods();
            for (Method method : declaredMethods) {

                for (Class<?> parameterClass : method.getParameterTypes()) {
                    if (Message.class.isAssignableFrom(parameterClass)) {
                        try {
                            Class cl = Class.forName(parameterClass.getName());
                            Method methodB = cl.getMethod("newBuilder");
                            Object obj = methodB.invoke(null, null);
                            Message.Builder msgBuilder = (Message.Builder) obj;
                            int msgId = msgBuilder.build().getDescriptorForType().getOptions().getExtension(Options.messageId);
                            controllerMap.put(msgId, new ControllerHandler(controller, method, msgId));
                        } catch (IllegalAccessException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        });


    }

    public Map<Integer, ControllerHandler> getControllerMap() {
        return controllerMap;
    }

}
