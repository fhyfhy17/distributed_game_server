package com.controller.interceptor;

import com.annotation.Interceptor;
import com.controller.ControllerHandler;
import com.pojo.Message;
import com.util.ReflectionUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HandlerExecutionChain {
    private static final Log log = LogFactory.getLog(HandlerExecutionChain.class);
    private ControllerHandler handler;
    private List<HandlerInterceptor> interceptorList;

    public HandlerExecutionChain() {
//        this.interceptorList = new ArrayList<>(  SpringUtils.getBeansOfType(HandlerInterceptor.class).values());
        @Data
        @AllArgsConstructor
        class InterceptorTempClass {
            private int order;
            private Class<?> clazz;
        }
        //扫描所有拦截器，取出order 用临时类排序，并返回所有拦截器实体
        this.interceptorList = ReflectionUtil.scan(HandlerInterceptor.class, Interceptor.class, "com.controller.interceptor")
                .stream().
                        map(x
                                -> {
                            Interceptor annotation = x.getAnnotation(Interceptor.class);
                            int order = annotation.order();
                            return new InterceptorTempClass(order, x);
                        }).sorted(Comparator.comparingInt(o -> o.order)).map(y ->
                        {
                            try {
                                return (HandlerInterceptor) y.getClazz().newInstance();
                            } catch (InstantiationException | IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                ).collect(Collectors.toList());
    }

    public void setHandler(ControllerHandler handler) {
        this.handler = handler;
    }

    public ControllerHandler getHandler() {
        return this.handler;
    }

    public void addInterceptor(HandlerInterceptor interceptor) {
        this.interceptorList.add(interceptor);
    }

    public void addInterceptors(HandlerInterceptor... interceptors) {
        if (!ObjectUtils.isEmpty(interceptors)) {
            CollectionUtils.mergeArrayIntoCollection(interceptors, this.interceptorList);
        }

    }

    public void addInterceptors(List<HandlerInterceptor> interceptors) {
        if (!ObjectUtils.isEmpty(interceptors)) {
            this.interceptorList.addAll(interceptors);
        }

    }

    public List<HandlerInterceptor> getInterceptors() {
        return this.interceptorList;
    }

    public boolean applyPreHandle(Message message) throws Exception {
        if (!ObjectUtils.isEmpty(interceptorList)) {
            for (int i = 0; i < interceptorList.size(); i++) {
                HandlerInterceptor interceptor = interceptorList.get(i);
                if (!interceptor.preHandle(message, this.handler)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void applyPostHandle(Message message, com.google.protobuf.Message result) throws Exception {
        if (!ObjectUtils.isEmpty(interceptorList)) {
            for (int i = 0; i < interceptorList.size(); i++) {
                HandlerInterceptor interceptor = interceptorList.get(i);
                interceptor.postHandle(message, this.handler, result);
            }
        }
    }


    public String toString() {
        Object handler = this.getHandler();
        StringBuilder sb = new StringBuilder();
        sb.append("HandlerExecutionChain with handler [").append(handler).append("]");

        if (!ObjectUtils.isEmpty(this.interceptorList)) {
            sb.append(" and ").append(this.interceptorList.size()).append(" interceptor");
            if (this.interceptorList.size() > 1) {
                sb.append("s");
            }
        }

        return sb.toString();
    }
}