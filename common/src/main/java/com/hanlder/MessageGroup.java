package com.hanlder;

import com.net.msg.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class MessageGroup {

    final transient static Logger log = LoggerFactory.getLogger(MessageGroup.class);
    private volatile boolean running = false;
    private int handlerCount = 16; // 执行器数量
    private String name;
    public List<MessageThreadHandler> hanlderList = new ArrayList<>();

    public MessageGroup(String name) {
        this.name = name;
    }

    public MessageGroup(String name, int handlerCount) {
        this.name = name;
        this.handlerCount = handlerCount;
    }

    public void startup() {
        // 正在运行
        if (running) {
            return;
        }

        // 开始启动
        running = true;

        // 初始化hanlder
        this.initHandlers();
    }

    private void initHandlers() {
        for (int i = 0; i < this.handlerCount; i++) {
            MessageThreadHandler handler = getMessageThreadHandler();
            new Thread(handler, this.name + i).start();
            hanlderList.add(handler);
        }
    }

    public abstract MessageThreadHandler getMessageThreadHandler();

    public void messageReceived(Message msg) {
        // 分配执行器执行
        int index = Math.abs(msg.getUid().hashCode()) % handlerCount;
        MessageThreadHandler handler = hanlderList.get(index);
        handler.messageReceived(msg);
    }

}
