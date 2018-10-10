package com.manager;

import com.enums.CacheNameEnum;
import com.pojo.ConnectUser;
import com.util.ContextUtil;
import org.apache.ignite.IgniteCache;


public class ConnectUserManger {
    public static IgniteCache<String, ConnectUser> getConnectUserCache() {

        IgniteCache<String, ConnectUser> cache = ContextUtil.ignite.cache(CacheNameEnum.session.name());
        return cache;
    }


}
