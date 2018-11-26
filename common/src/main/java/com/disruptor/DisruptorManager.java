package com.disruptor;

import com.lmax.disruptor.RingBuffer;

import java.util.HashMap;
import java.util.Map;

public class DisruptorManager {
    private static Map<DisruptorEnum, DisruptorCreater> disMap = new HashMap<>();

    public static DisruptorCreater getDisruptorCreater(DisruptorEnum type) {
        return disMap.get(type);
    }

    public static RingBuffer getRingBuffer(DisruptorEnum type) {
        DisruptorCreater disruptorCreater = disMap.get(type);
        if (disruptorCreater == null) {
            return null;
        }
        return disruptorCreater.getRingBuffer();
    }

    public static void addDisruptor(DisruptorEnum type, DisruptorCreater disruptorCreater) {
        disMap.put(type, disruptorCreater);
    }

}
