package com.util;

public class CountUtil {
    private static int count;
    private static long start;

    public static void start() {
        count = 0;
    }

    public static void count() {
        count++;
        if (count == 1) {
            start = System.currentTimeMillis();
        }
        if (count == 10000000) {
            System.out.println("共用时：" + (System.currentTimeMillis() - start));
        }
    }
}
