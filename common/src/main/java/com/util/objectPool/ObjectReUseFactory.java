package com.util.objectPool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.poi.ss.formula.functions.T;

public class ObjectReUseFactory extends GenericObjectPool<T> {

    public ObjectReUseFactory(PooledObjectFactory<T> factory, GenericObjectPoolConfig config) {
        super(factory, config);
    }
}
