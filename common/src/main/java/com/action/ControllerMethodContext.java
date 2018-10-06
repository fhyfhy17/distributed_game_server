package com.action;

import com.action.resolver.MethodParameter;
import com.action.resolver.ResolverManager;
import com.pojo.Message;
import com.util.SpringUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Objects;

@Data
public class ControllerMethodContext {
    private static final Logger log = LoggerFactory.getLogger(ControllerMethodContext.class);
    private BaseController action;
    private Method method;
    private MethodParameter[] parameters;

    public ControllerMethodContext(BaseController action, Method method) {
        this.action = action;
        this.method = method;
        Class<?>[] parameterTypes = this.method.getParameterTypes();
        MethodParameter[] parameters = new MethodParameter[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = new MethodParameter(method, i, this.method.getParameterTypes()[i]);
        }
        this.parameters = parameters;
    }

    public Object invokeForController(Message message) throws Exception {
        Object[] args = getMethodArgumentValues(message);
        return this.method.invoke(this.action, args);

    }

    private Object[] getMethodArgumentValues(Message message) throws Exception {
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < args.length; i++) {
            MethodParameter methodParameter = this.parameters[i];
            ResolverManager resolverManager = SpringUtils.getBean(ResolverManager.class);
            Object o = resolverManager.resolve(methodParameter, message);
            if (Objects.isNull(o)) {
                throw new IllegalArgumentException("出现了不支持的参数 = " + methodParameter);
            }
            args[i] = o;
        }
        return args;
    }

}
