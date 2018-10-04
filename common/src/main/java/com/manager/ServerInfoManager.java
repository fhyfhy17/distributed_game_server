package com.manager;

import cn.hutool.core.util.RandomUtil;
import com.enums.ServerTypeEnum;
import com.pojo.ServerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ServerInfoManager {
    private static final Logger log = LoggerFactory.getLogger(ServerInfoManager.class);
    private static ConcurrentHashMap<String, ServerInfo> serverInfos = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, ServerInfo> getAllServiceStatus() {
        return serverInfos;
    }

    public static void addServerInfo(ServerInfo serverInfo) {
        if (serverInfo != null) {
            ServerInfoManager.serverInfos.put(serverInfo.getServerId(), serverInfo);
        }
        log.info("增加节点={} 所有节点={}", serverInfo.getServerId(), ServerInfoManager.serverInfos.values());
    }

    public static void removeServerInfo(String serviceId) {
        if (serviceId != null) {
            serverInfos.remove(serviceId);
        }
        log.info("删除节点={} 所有节点={}", serviceId, serverInfos.values());
    }


    /**
     * 随机一个服务
     */
    public static ServerInfo randomServerInfo(ServerTypeEnum serviceType) {
        List<ServerInfo> list = serverInfos.values().stream().filter(
                x -> x.getServerType() == serviceType
        ).collect(Collectors.toList());
        return list.size() < 1 ? null : RandomUtil.randomEle(list);
    }

    public static List<ServerInfo> getServerInfosByType(ServerTypeEnum serviceType) {
        return serverInfos.values().stream().filter(
                x -> x.getServerType() == serviceType
        ).collect(Collectors.toList());
    }

    public static ServerInfo getServerInfo(String serverId) {
        return serverInfos.values().stream().filter(
                x -> x.getServerId() == serverId
        ).findAny().orElse(null);
    }

    public static String hashChooseServer(String uid, ServerTypeEnum typeEnum) {
        List<ServerInfo> list = getServerInfosByType(typeEnum);
        if (list.size() < 1) {
            log.error("所有 {} 服务器都挂了", typeEnum);
            return null;
        }
        int index = uid.hashCode() % list.size();
        ServerInfo info = list.get(index);
        return info.getServerId();
    }

}
