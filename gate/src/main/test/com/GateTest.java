package com;

import com.net.msg.LOGIN_MSG;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class GateTest {

    @Test
    public void run() throws IOException, InterruptedException {
        Socket s = new Socket("192.168.1.4", 10001);
        System.out.println(s.isConnected());
        LOGIN_MSG.CTS_LOGIN.Builder builder = LOGIN_MSG.CTS_LOGIN.newBuilder();
        builder.setUsername("aaa");
        builder.setPassword("1");
        OutputStream outputStream = s.getOutputStream();//获取一个输出流，向服务端发送信息
        outputStream.write(builder.build().toByteArray());
        outputStream.flush();
        outputStream.close();
//        s.shutdownOutput();//关闭输出流
        CountDownLatch c = new CountDownLatch(1);
        c.await();
    }
}