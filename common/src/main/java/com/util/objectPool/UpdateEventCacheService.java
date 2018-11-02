package com.util.objectPool;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.poi.ss.formula.functions.T;

@Slf4j
public class UpdateEventCacheService {

    public static ObjectReUseFactory objectReUseFactory;

    private static int size = 1024;
    private static int maxSize = 1024 * 32;

    private static boolean poolOpenFlag;

    public static void start() {
        int size = getSize();
        int maxSize = getMaxSize();
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(maxSize);
        genericObjectPoolConfig.setMaxIdle(maxSize);
        genericObjectPoolConfig.setMinIdle(size);
        long time = 1000 * 30;
        genericObjectPoolConfig.setMaxWaitMillis(time);
        genericObjectPoolConfig.setSoftMinEvictableIdleTimeMillis(time);
        genericObjectPoolConfig.setTestOnReturn(true);

        objectReUseFactory = new ObjectReUseFactory(new ObjectPoolFactory(), genericObjectPoolConfig);
    }

    public static void stop() {
        if (objectReUseFactory != null) {
            objectReUseFactory.close();
        }
    }

    public static T createObject() {

        if (!poolOpenFlag) {
            T t = null;
            try {
                t = T.class.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return t;
        }

        try {
            return objectReUseFactory.borrowObject();
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        return null;
    }

    public static void releaseObject(T t) {
        if (!poolOpenFlag) {
            return;
        }
        objectReUseFactory.returnObject(t);
    }

    public static int getSize() {
        return size;
    }

    public static void setSize(int size) {
        UpdateEventCacheService.size = size;
    }

    public static int getMaxSize() {
        return maxSize;
    }

    public static void setMaxSize(int maxSize) {
        UpdateEventCacheService.maxSize = maxSize;
    }

    public static boolean isPoolOpenFlag() {
        return poolOpenFlag;
    }

    public static void setPoolOpenFlag(boolean poolOpenFlag) {
        UpdateEventCacheService.poolOpenFlag = poolOpenFlag;
    }
}
