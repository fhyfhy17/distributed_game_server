package com.action;

import com.util.SpringUtils;
import com.google.common.collect.Maps;
import com.google.protobuf.Message;
import com.net.msg.Options;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class ActionFactory {

    public Map<Integer, ActionMethodContext> actionMap = Maps.newHashMap();

    @PostConstruct
    private void init() {
        Map<String, BaseController> allActions = SpringUtils.getBeansOfType(BaseController.class);
        allActions.values().forEach(action -> {
            Method[] declaredMethods = action.getClass().getDeclaredMethods();
            for (Method method : declaredMethods) {

                for (Class<?> parameterClass : method.getParameterTypes()) {
                    if (Message.class.isAssignableFrom(parameterClass)) {
                        try {
                            Class cl = Class.forName(parameterClass.getName());
                            Method methodB = cl.getMethod("newBuilder");
                            Object obj = methodB.invoke(null, null);
                            Message.Builder msgBuilder = (Message.Builder) obj;
                            Integer msgId = msgBuilder.build().getDescriptorForType().getOptions().getExtension(Options.messageId);
                            actionMap.put(msgId, new ActionMethodContext(action, method));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        });
    }

    public Map<Integer, ActionMethodContext> getActionMap() {
        return actionMap;
    }

    @Data
    @AllArgsConstructor
    public static class ActionMethodContext {
        private BaseController action;
        private Method method;

    }

    @Data
    @AllArgsConstructor
    public static class UidContext {
        private String uid;
        private String from;

    }
}
