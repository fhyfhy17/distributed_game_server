package com.util.objectPool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.poi.ss.formula.functions.T;

public class ObjectPoolFactory implements PooledObjectFactory<T> {
    @Override
    public PooledObject<T> makeObject() throws Exception {
//        System.out.println("makeObject updateEvent");
        T t = T.class.newInstance();
        return new DefaultPooledObject<>(t);
    }

    @Override
    public void destroyObject(PooledObject<T> p) throws Exception {
//        System.out.println("destroyObject updateEvent");
        T t = p.getObject();
        t = null;
    }

    @Override
    public boolean validateObject(PooledObject<T> p) {
//        System.out.println("validateObject updateEvent");
        return false;
    }

    @Override
    public void activateObject(PooledObject<T> p) throws Exception {
//        System.out.println("active updateEvent");
    }

    @Override
    public void passivateObject(PooledObject<T> p) throws Exception {
//        System.out.println("passivate updateEvent");
    }
}
