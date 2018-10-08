package com.hanlder;

import com.controller.ControllerHandler;
import com.controller.interceptor.HandlerExecutionChain;
import com.pojo.Message;
import com.util.ContextUtil;
import com.util.SpringUtils;
import io.vertx.core.logging.LoggerFactory;
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

                ControllerHandler m = ContextUtil.controllerFactory.getControllerMap().get(cmdId);
                if (m == null) {
                    throw new IllegalStateException("收到不存在的消息，消息ID=" + cmdId);
                }
                HandlerExecutionChain chain = SpringUtils.getBean(HandlerExecutionChain.class);
                chain.setHandler(m);
                if (!chain.applyPreHandle(message)) {
                    continue;
                }
                //针对method的每个参数进行处理， 处理多参数,返回result
                com.google.protobuf.Message result = (com.google.protobuf.Message) m.invokeForController(message);
                chain.applyPostHandle(message, result);
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
