package com.hanlder;

import com.action.ControllerMethodContext;
import com.manager.VertxMessageManager;
import com.net.msg.Options;
import com.pojo.Message;
import com.util.ContextUtil;
import io.vertx.core.logging.LoggerFactory;

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

                ControllerMethodContext m = ContextUtil.controllerFactory.getControllerMap().get(cmdId);
                if (m == null) {
                    log.error("收到不存在的消息，消息ID={}", cmdId);
                    continue;
                }
                //针对method的每个参数进行处理， 处理多参数,返回result
                com.google.protobuf.Message result = (com.google.protobuf.Message) m.invokeForController(message);
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


    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }


}
