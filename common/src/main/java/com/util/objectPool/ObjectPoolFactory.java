package com.util.objectPool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.poi.ss.formula.functions.T;

public class ObjectPoolFactory implements PooledObjectFactory<T> {
    @Override
    public PooledObject<T> makeObject() throws Exception {
        T t = T.class.newInstance();
        return new DefaultPooledObject<>(t);
    }

    @Override
    public void destroyObject(PooledObject<T> p) throws Exception {
        T t = p.getObject();
        t = null;
    }

    @Override
    public boolean validateObject(PooledObject<T> p) {
        return false;
    }

    @Override
    public void activateObject(PooledObject<T> p) throws Exception {
    }

    @Override
    public void passivateObject(PooledObject<T> p) throws Exception {
    }
}
