package com.manager;

import com.enums.ServerTypeEnum;
import com.pojo.ServerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ServerInfoManager {
    private static Random rand = new Random();
    private static ConcurrentHashMap<String, ServerInfo> servicestatus = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, ServerInfo> getAllServiceStatus() {
        return servicestatus;
    }

    public static void addServerInfo(ServerInfo serverInfo) {
        if (serverInfo != null)
            servicestatus.put(serverInfo.getServerId(), serverInfo);
        System.out.println("服务状态最新(add)：" + servicestatus.values());
    }

    public static void removeServerInfo(String serviceId) {
        if (serviceId != null)
            servicestatus.remove(serviceId);
        System.out.println("服务状态最新(remove)：" + servicestatus.values());
    }

    /**
     * 随机一个服务
     */
    public static ServerInfo randomServerInfo(ServerTypeEnum serviceType) {
        // 现在有几个当前的服务状态
        List<ServerInfo> list = new ArrayList<>();
        for (ServerInfo serverInfo : servicestatus.values()) {
            if (serverInfo.getServerType() == serviceType) {
                list.add(serverInfo);
            }
        }

        return list.size() == 1 ? list.get(0) : list.get(randNum(0, list.size() - 1));
    }

    public static List<ServerInfo> getServerInfosByType(ServerTypeEnum serviceType) {
        // 现在有几个当前的服务状态
        List<ServerInfo> list = new ArrayList<>();
        for (ServerInfo serverInfo : servicestatus.values()) {
            if (serverInfo.getServerType() == serviceType) {
                list.add(serverInfo);
            }
        }
        return list;
    }

    public static ServerInfo getServerInfo(String serviceId) {
        for (ServerInfo serverInfo : servicestatus.values()) {
            if (serverInfo.getServerId().equals(serviceId)) {
                return serverInfo;
            }
        }
        return null;
    }

    public static String hashChooseServer(String uid, ServerTypeEnum typeEnum) {
        List<ServerInfo> list = getServerInfosByType(typeEnum);
        if (list.size() < 1) {
            return "";
        }
        int index = uid.hashCode() % list.size();
        ServerInfo info = list.get(index);
        return info.getServerId();
    }

    private static int randNum(int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }
}
