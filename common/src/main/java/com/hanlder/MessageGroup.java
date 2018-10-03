package com.hanlder;

import com.net.msg.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class MessageGroup {

    private volatile boolean running = false;
    private int handlerCount = 16; // 执行器数量

    public List<MessageThreadHandler> hanlderList = new ArrayList<>();

    public void startup() {
        // 正在运行
        if (running) {
            return;
        }

        // 开始启动
        running = true;

        // 初始化hanlder
        this.initHandlers(handlerCount, "消息");
    }

    public abstract void initHandlers(int count, String name);
//    private void initHandlers(int count, String name) {
//        for (int i = 0; i < count; i++) {
//            MessageThreadHandler hanlder = new MessageThreadHandler(name + i);
//            hanlder.startup();
//            hanlderList.add(hanlder);
//        }
//    }

    public void messageReceived(Message msg) {
        // 分配执行器执行
        int index = Math.abs(msg.getUid().hashCode()) % handlerCount;
        MessageThreadHandler hanlder = hanlderList.get(index);
        hanlder.messageReceived(msg);
    }

}
