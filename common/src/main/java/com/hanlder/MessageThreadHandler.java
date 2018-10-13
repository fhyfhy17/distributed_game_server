package com.hanlder;

import com.controller.ControllerHandler;
import com.controller.interceptor.HandlerExecutionChain;
import com.pojo.Message;
import com.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class MessageThreadHandler implements Runnable {
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

                ControllerHandler handler = ContextUtil.controllerFactory.getControllerMap().get(cmdId);
                if (handler == null) {
                    throw new IllegalStateException("收到不存在的消息，消息ID=" + cmdId);
                }
                //拦截器前
                if (!HandlerExecutionChain.applyPreHandle(message, handler)) {
                    continue;
                }
                //针对method的每个参数进行处理， 处理多参数,返回result
                com.google.protobuf.Message result = (com.google.protobuf.Message) handler.invokeForController(message);
                //拦截器后
                HandlerExecutionChain.applyPostHandle(message, result, handler);
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }


    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }


}
