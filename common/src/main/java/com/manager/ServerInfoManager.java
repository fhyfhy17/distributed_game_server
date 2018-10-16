package com.manager;

import cn.hutool.core.util.RandomUtil;
import com.enums.ServerTypeEnum;
import com.net.msg.RemoteNode;
import com.pojo.ServerInfo;
import com.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class ServerInfoManager {

    private static ConcurrentHashMap<String, ServerInfo> servicestatus = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, RemoteNode> remotes = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, ServerInfo> getAllServerInfos() {
        return servicestatus;
    }

    public static void addServerInfo(ServerInfo serverInfo) {
        if (serverInfo != null) {

            String hostAddress = serverInfo.getIp() + ":" + serverInfo.getPort();

            if (serverInfo.getServerId() == ContextUtil.id) {
                log.info("这是本地");
            } else {
                log.info("这是远端");
                RemoteNode remoteNode = new RemoteNode(hostAddress);
                remotes.put(serverInfo.getServerId(),remoteNode);
                remoteNode.startup();
            }


            servicestatus.put(serverInfo.getServerId(), serverInfo);
        }
        log.info("服务加入={}： 所有服务器状态={}", serverInfo, servicestatus.values());
    }

    public static void removeServerInfo(String serviceId) {
        if (serviceId != null) {
            ServerInfo serverInfo = servicestatus.remove(serviceId);
            RemoteNode remoteNode = remotes.remove(serviceId);
            remoteNode.stop();
        }
        log.info("服务删除={}： 所有服务器状态={}", serviceId, servicestatus.values());
    }

    /**
     * 随机一个服务
     */
    public static ServerInfo randomServerInfo(ServerTypeEnum serverType) {
        List<ServerInfo> list = getAllServerInfos().values().stream().filter(
                x -> x.getServerType() == serverType
        ).collect(Collectors.toList());
        return list.size() < 1 ? null : RandomUtil.randomEle(list);
    }

    public static List<ServerInfo> getServerInfosByType(ServerTypeEnum serverType) {
        return getAllServerInfos().values().stream().filter(
                x -> x.getServerType() == serverType
        ).collect(Collectors.toList());
    }

    public static ServerInfo getServerInfo(String serverId) {

        return servicestatus.get(serverId);
    }

    public static RemoteNode getRemote(String serverId) {

        return remotes.get(serverId);
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

    public static void printServerStatus(ServerInfo serverInfo, boolean join) {
        String action = join ? "加入" : "离开";
        log.info(action + " 服务器 serverType= {} , serverId= {}", serverInfo.getServerId(), serverInfo.getServerType());
        log.info("当前所有的服务器={}", getAllServerInfos());
    }

}
