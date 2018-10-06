package com.action;

import com.google.common.collect.Maps;
import com.google.protobuf.Message;
import com.net.msg.Options;
import com.util.SpringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class ControllerFactory {

    public Map<Integer, ControllerMethodContext> controllerMap = Maps.newHashMap();

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
                            Integer msgId = msgBuilder.build().getDescriptorForType().getOptions().getExtension(Options.messageId);
                            controllerMap.put(msgId, new ControllerMethodContext(controller, method));
                        } catch (IllegalAccessException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        });
    }

    public Map<Integer, ControllerMethodContext> getControllerMap() {
        return controllerMap;
    }

}
