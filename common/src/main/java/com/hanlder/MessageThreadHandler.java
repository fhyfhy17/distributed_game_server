package com.hanlder;

import com.action.ActionFactory;
import com.google.protobuf.InvalidProtocolBufferException;
import com.manager.VertxMessageManager;
import com.net.msg.Message;
import com.net.msg.Options;
import com.util.ContextUtil;
import io.vertx.core.logging.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageThreadHandler implements Runnable {
    final static io.vertx.core.logging.Logger log = LoggerFactory.getLogger(MessageThreadHandler.class);
    // 执行器ID
    protected String handlerId;
    // 心跳频率10毫秒
    private int interval = 10;


    protected final ConcurrentLinkedQueue<Message> pulseQueues = new ConcurrentLinkedQueue<>();


    @Override
    public void run() {
        for (; ; ) {
            long startTime = System.currentTimeMillis();

            // 执行心跳
            pulse();

            long finishTime = System.currentTimeMillis();
            long timeRunning = finishTime - startTime;
            try {
                if (timeRunning < interval) {
                    Thread.sleep(interval - timeRunning);
                } else {
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
            }
        }
    }

    public void messageReceived(Message msg) {
        pulseQueues.add(msg);
    }


    public void pulse() {
        while (!pulseQueues.isEmpty()) {
            try {
                Message message = pulseQueues.poll();
                final int cmdId = message.getId();

                ActionFactory.ActionMethodContext m = ContextUtil.actionFactory.getActionMap().get(cmdId);
                if (m == null) {
                    log.error("收到不存在的消息，消息ID={}", cmdId);
                }
                //针对method的每个参数进行处理， 处理多参数,返回result
                Object o = m.getMethod().invoke(m.getAction(), dealMethod(m.getMethod(), message));
                com.google.protobuf.Message result = (com.google.protobuf.Message) o;
                if (!Objects.isNull(result)) {
                    Message messageResult = buildMessage(result, message);
                    VertxMessageManager.sendMessage(message.getFrom(), messageResult);
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    private Message buildMessage(com.google.protobuf.Message resultMessage, Message message) {
        Message messageResult = new Message();
        messageResult.setId(resultMessage.getDescriptorForType().getOptions().getExtension(Options.messageId));
        messageResult.setUid(message.getUid());
        messageResult.setFrom(ContextUtil.id);
        messageResult.setData(resultMessage.toByteArray());
        return messageResult;
    }

    private Object[] dealMethod(Method method, Message message) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, InvalidProtocolBufferException {
        Object[] objs = new Object[method.getParameterCount()];
        for (int i = 0; i < method.getParameters().length; i++) {
            Parameter parameter = method.getParameters()[i];
            //对协议参数的处理
            if (com.google.protobuf.Message.class.isAssignableFrom(parameter.getType())) {
                Class<? extends com.google.protobuf.Message> messageProto = null;
                messageProto = (Class<? extends com.google.protobuf.Message>) parameter.getType();
                Constructor<? extends com.google.protobuf.Message> cons = messageProto.getDeclaredConstructor();
                cons.setAccessible(true);
                objs[i] = cons.newInstance().getParserForType().parseFrom(message.getData());
            } else if (ActionFactory.UidContext.class.isAssignableFrom(parameter.getType())) {
                ActionFactory.UidContext u = new ActionFactory.UidContext(message.getUid(), message.getFrom());
                objs[i] = u;
            } else {
                //TODO 这里打算返回 Player
            }
        }
        return objs;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }


}
