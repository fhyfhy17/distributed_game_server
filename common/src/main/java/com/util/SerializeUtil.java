package com.util;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.net.msg.LOGIN_MSG;
import com.pojo.Message;
import com.pojo.NettyMessage;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class SerializeUtil {
    private static Kryo k = new Kryo();

    static {
        k.register(Message.class);
        k.setReferences(true);
        k.setInstantiatorStrategy(new StdInstantiatorStrategy());
        k.setDefaultSerializer(DefaultSerializers.ByteSerializer.class);

        k.register(NettyMessage.class);
    }

    //kyro 1  proto 2 fast 3
    public static final int type = 2;


    public static Message stm(byte[] s) {
        if (type == 1) {
            return kryoStm(s);
        }
        else if (type == 2) {
            return protoStm(s);
        } else {
            return fastStm(s);
        }
//        return null;
    }

    public static byte[] mts(Message m) {
        if (type == 1) {
            return kryoMts(m);
        }
        else if (type == 2) {
            return protoMts(m);
        } else {
            return fastMts(m);
        }
//        return null;
    }

    public static Message kryoStm(byte[] s) {
        Message m = null;
        try (
                ByteArrayInputStream bais = new ByteArrayInputStream(s);
                Input input = new Input(bais)

        ) {
            m = k.readObject(input, Message.class);
        } catch (IOException e) {
            log.error("", e);
        }

        return m;
    }

    public static byte[] kryoMts(Message m) {


        byte[] bys = null;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            @Cleanup Output output = new Output(baos);
            k.writeObject(output, m);
            output.flush();
            bys = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bys;
    }


    public static byte[] fastMts(Message m) {
        String s = JSON.toJSONString(m);
        return s.getBytes();
    }

    public static Message fastStm(byte[] s) {
        return JSON.parseObject(s, Message.class);
    }

    static LOGIN_MSG.MyMessage.Builder builder = LOGIN_MSG.MyMessage.newBuilder();

    public static byte[] protoMts(Message m) {
        builder.setUid(m.getUid());
        builder.setId(m.getId());
        builder.setData(ByteString.copyFrom(m.getData()));
        builder.setFrom(m.getFrom());
        byte[] bytes = builder.build().toByteArray();
        return bytes;
    }

    public static Message protoStm(byte[] s) {
        Message m2 = new Message();
        try {
            LOGIN_MSG.MyMessage m = LOGIN_MSG.MyMessage.parseFrom(s);

            m2.setId(m.getId());
            m2.setUid(m.getUid());
            m2.setData(m.getData().toByteArray());
            m2.setFrom(m.getFrom());
            return m2;

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return m2;
    }

    public static void main(String[] args) throws IOException {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            sb.append("这是测试");
        }
        Message m = new Message();
        m.setId(1);
        m.setFrom("a");
        m.setUid(sb.toString());
        byte[] bbb = new byte[800];
        m.setData(bbb);
        int count = 100000;

        byte[] kryoString = kryoMts(m);
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            kryoMts(m);
        }
        long end2 = System.currentTimeMillis();
        log.info("kryo msg to String = {}", end2 - start2);

        long start5 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            fastMts(m);
        }
        long end5 = System.currentTimeMillis();

        log.info("fast msg to String = {}", end5 - start5);


        long start11 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            protoMts(m);
        }
        long end11 = System.currentTimeMillis();

        log.info("proto msg to String = {}", (end11 - start11));

        long start4 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            kryoStm(kryoString);
        }
        long end4 = System.currentTimeMillis();

        log.info("kryo String to msg  = {}", (end4 - start4));


        byte[] fastString = fastMts(m);
        long start6 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            fastStm(fastString);
        }
        long end6 = System.currentTimeMillis();
        log.info("fast String to msg = {}", (end6 - start6));


        byte[] protoString = protoMts(m);
        long start12 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            protoStm(protoString);
        }
        long end12 = System.currentTimeMillis();
        log.info("proto String to msg = {}", (end12 - start12));

        log.info("kryo String大小 = {}", kryoString.length);
        log.info("fast String大小 = {}", fastString.length);
        log.info("prorto String大小 = {}", protoString.length);

    }
}