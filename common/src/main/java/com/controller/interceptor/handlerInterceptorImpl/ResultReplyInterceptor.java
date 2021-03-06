package com.controller.interceptor.handlerInterceptorImpl;

import com.controller.ControllerHandler;
import com.controller.interceptor.HandlerInterceptor;
import com.manager.VertxMessageManager;
import com.net.msg.Options;
import com.pojo.Message;
import com.util.ContextUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Order(5)
@Component
//结果拦截器 （根据执行完消息返回的结果，执行回消息操作）
public class ResultReplyInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(Message message, ControllerHandler handler, com.google.protobuf.Message result) {
        if (Objects.isNull(result)) {
            return;
        }

        Message messageResult = buildMessage(result, message);
        VertxMessageManager.sendMessage(message.getFrom(), messageResult);

    }

    private Message buildMessage(com.google.protobuf.Message resultMessage, Message message) {
        Message messageResult = new Message();
        messageResult.setId(resultMessage.getDescriptorForType().getOptions().getExtension(Options.messageId));
        messageResult.setUid(message.getUid());
        messageResult.setFrom(ContextUtil.id);
        messageResult.setData(resultMessage.toByteArray());
        return messageResult;
    }
}
