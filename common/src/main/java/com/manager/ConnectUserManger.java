package com.manager;

import com.Constant;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.pojo.ConnectUser;
import com.util.ContextUtil;


public class ConnectUserManger {
    public static ConnectUser getConnectUser(String uid) {
        HazelcastInstance ins = Hazelcast.getHazelcastInstanceByName(ContextUtil.id);
        IMap<String, ConnectUser> map = ins.getMap(Constant.CONNECT_USER_MAP);
        return map.get(uid);
    }

    public static void saveConnectUser(ConnectUser connectUser) {
        HazelcastInstance ins = Hazelcast.getHazelcastInstanceByName(ContextUtil.id);
        IMap<String, ConnectUser> map = ins.getMap(Constant.CONNECT_USER_MAP);
        map.put(connectUser.getUid(), connectUser);
    }


}
