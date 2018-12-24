package com.handler;

import com.controller.ControllerFactory;
import com.controller.ControllerHandler;
import com.controller.interceptor.HandlerExecutionChain;
import com.exception.exceptionNeedSendToClient.ExceptionNeedSendToClient;
import com.pojo.Message;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class MessageThreadHandler implements Runnable {
    // 执行器ID
    protected String handlerId;
    // 心跳频率10毫秒
    private int interval = 10;

    private StopWatch stopWatch = new StopWatch();

    protected final ConcurrentLinkedQueue<Message> pulseQueues = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {
        for (; ; ) {
            stopWatch.start();

            // 执行心跳
            pulse();

            stopWatch.stop();

            try {
                if (stopWatch.getTime() < interval) {
                    Thread.sleep(interval - stopWatch.getTime());
                } else {
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
            } finally {
                stopWatch.reset();
            }
        }
    }

    public void messageReceived(Message msg) {
        pulseQueues.add(msg);
    }


    public void pulse() {
        while (!pulseQueues.isEmpty()) {
            ControllerHandler handler = null;
            Message message = null;
            try {
                message = pulseQueues.poll();
                final int cmdId = message.getId();

                handler = ControllerFactory.getControllerMap().get(cmdId);
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
            } catch (ExceptionNeedSendToClient exceptionNeedSendToClient) {
                Class<?> returnType = handler.getMethod().getReturnType();
                if (returnType.isAssignableFrom(com.google.protobuf.Message.class)) {
                    HandlerExecutionChain.applyPostHandle(message, null, handler);
                }
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
