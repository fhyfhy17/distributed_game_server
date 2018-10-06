package com;

import com.net.msg.LOGIN_MSG;
import com.pojo.Message;
import io.netty.channel.Channel;

import java.util.Scanner;

public class ClientSession {
    private static final ClientSession session = new ClientSession();

    public static ClientSession getInstance() {
        return session;
    }

    public static void init(final Channel channel) {
        ClientSession.getInstance().setUid("a123");
        ClientSession.getInstance().setChannel(channel);
        System.out.println("客户端打开连接");
        new Thread(new Runnable() {

            @Override
            public void run() {
                @SuppressWarnings("resource")
                Scanner input = new Scanner(System.in);
                System.out.println("输入1登录 输入2登出");
                while (input.hasNext()) {
                    System.out.println("输入1登录 输入2登出");
                    String line = input.next();
                    if (!("1".equals(line) || "2".equals(line))) {
                        System.out.println("输入错误，请重新输入1登录 输入2登出   ");
                    }
                    if ("1".equals(line)) {
                        LOGIN_MSG.CTS_LOGIN.Builder builder = LOGIN_MSG.CTS_LOGIN.newBuilder();
                        builder.setUsername("aaa");
                        builder.setPassword("1");

                        Message m = new Message();
                        m.setId(10001);
                        m.setData(builder.build().toByteArray());
                        channel.writeAndFlush(m);
                    }

                    if ("2".equals(line)) {
                        LOGIN_MSG.CTS_TEST.Builder builder = LOGIN_MSG.CTS_TEST.newBuilder();
                        builder.setWord("啊啊等等");
                        Message m = new Message();
                        m.setId(10005);
                        m.setData(builder.build().toByteArray());
                        channel.writeAndFlush(m);
                    }
                }
            }
        }).start();

    }

    private String uid;
    private Channel channel;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
