package com.util;

import com.hazelcast.internal.serialization.impl.DefaultSerializationServiceBuilder;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import com.hazelcast.spi.serialization.SerializationService;
import com.net.msg.Message;
import io.vertx.core.json.Json;
import lombok.Data;

import java.io.IOException;

public class SerializeUtil {

    private static SerializationService serializationService = new DefaultSerializationServiceBuilder().build();

    public static <T> T toObj(String s,Class<?> clazz) {
//        return (T) serializationService.toObject(new HeapData(s.getBytes()));
        return  (T) Json.decodeValue(s,clazz);
    }

    public static <T> String objToString(T t) {
//        return new String(serializationService.toData(t).toByteArray());
        return Json.encode(t);
    }


    public static void main(String[] args) {
        Info i = new Info();
        i.setUid("aaa");
//        Object a = serializationService.toData(i);
//        Info i3 = (Info)serializationService.toObject(a);
//        String s = objToString(i);
//        Info i2 = toObj(s);
        Message m = new Message();
        m.setUid("1");
        m.setId(2);
        m.setFrom("a");
        m.setData("sasdif".getBytes());
        String  b  =Json.encode(m);

        Message m2  =Json.decodeValue(b,Message.class);

        System.out.println(m2);
    }

    @Data
    public static class Info implements DataSerializable {
        private String uid; // uid


        @Override
        public void writeData(ObjectDataOutput out) throws IOException {
            out.writeUTF(uid);
        }

        @Override
        public void readData(ObjectDataInput in) throws IOException {
            this.uid = in.readUTF();
        }
    }
}