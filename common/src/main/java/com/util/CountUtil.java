package com.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
@Slf4j
public class CountUtil {
    private static AtomicInteger count;
    private static long start;

    public static void start() {
        count = new AtomicInteger(0);
    }

    public static void count() {
        count.addAndGet(1);
        if (count.intValue() == 1) {
            start = System.currentTimeMillis();
        }
        log.info(String.valueOf(count.intValue()));
        if (count.intValue() == 1000000) {
            System.out.println("共用时：" + (System.currentTimeMillis() - start));
        }
    }
}
