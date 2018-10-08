package com.util;

import com.alibaba.fastjson.JSON;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.net.msg.LOGIN_MSG;
import com.pojo.Message;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public class SerializeUtil {
    private static Kryo k = new Kryo();

    static {
        k.register(Message.class);
        k.setReferences(true);
        k.setInstantiatorStrategy(new StdInstantiatorStrategy());

    }


    //TODO 有机会还是要换个序列化方式 ，json还是太挫
    //TODO 打脸 实际结果fastjson一点都不挫。比 gson kryo proto protostuff都要快得多,体积也小很小~~~~


    //kyro 1  proto 2 fast 3
    public static final int type = 3;


    public static Message stm(String s) {
        if (type == 1) {
            return kryoStm(s);
        } else if (type == 2) {
            return protoStm(s);
        } else {
            return fastStm(s);
        }
    }

    public static String mts(Message m) {
        if (type == 1) {
            return kryoMts(m);
        } else if (type == 2) {
            return protoMts(m);
        } else {
            return fastMts(m);
        }
    }

    public static Message kryoStm(String s) {
        Message m = null;
        try (
                ByteArrayInputStream bais = new ByteArrayInputStream(java.util.Base64.getDecoder().decode(s.getBytes()));
                Input input = new Input(bais)

        ) {
            m = k.readObject(input, Message.class);
        } catch (IOException e) {
            log.error("", e);
        }

        return m;
    }

    public static String kryoMts(Message m) {


        byte[] bys = null;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            @Cleanup Output output = new Output(baos);
            k.writeObject(output, m);
            output.flush();
            bys = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new String(java.util.Base64.getEncoder().encode(bys));
    }


    public static String fastMts(Message m) {
        return JSON.toJSONString(m);
    }

    public static Message fastStm(String s) {
        return JSON.parseObject(s, Message.class);
    }

    static LOGIN_MSG.MyMessage.Builder builder = LOGIN_MSG.MyMessage.newBuilder();

    public static String protoMts(Message m) {

        builder.setUid(m.getUid());
        builder.setId(m.getId());
        builder.setData(ByteString.copyFrom(m.getData()));
        builder.setFrom(m.getFrom());
        return new String(Base64.getEncoder().encode(builder.build().toByteArray()));
    }

    public static Message protoStm(String s) {
        Message m2 = new Message();
        try {
            LOGIN_MSG.MyMessage m = LOGIN_MSG.MyMessage.parseFrom(Base64.getDecoder().decode(s.getBytes()));

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
        for (int i = 0; i < 10000; i++) {
            sb.append("这是测试");
        }
        Message m = new Message();
        m.setId(1);
        m.setFrom("a");
        m.setUid(sb.toString());
        byte[] bbb = new byte[8];
        m.setData(bbb);
        int count = 10000;

        String kryoString = kryoMts(m);
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


        String fastString = fastMts(m);
        long start6 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            fastStm(fastString);
        }
        long end6 = System.currentTimeMillis();
        log.info("fast String to msg = {}", (end6 - start6));


        String protoString = protoMts(m);
        long start12 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            protoStm(protoString);
        }
        long end12 = System.currentTimeMillis();
        log.info("proto String to msg = {}", (end12 - start12));

        log.info("kryo String大小 = {}", kryoString.length());
        log.info("fast String大小 = {}", fastString.length());
        log.info("prorto String大小 = {}", protoString.length());

    }
}