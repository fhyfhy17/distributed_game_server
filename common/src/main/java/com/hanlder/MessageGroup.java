package com.hanlder;

import com.pojo.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public abstract class MessageGroup {

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
