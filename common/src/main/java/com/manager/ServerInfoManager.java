package com.manager;

import cn.hutool.core.util.RandomUtil;
import com.Constant;
import com.enums.ServerTypeEnum;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.pojo.ServerInfo;
import com.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ServerInfoManager {


    public static List<ServerInfo> getAllServerInfos() {
        HazelcastInstance ins = Hazelcast.getHazelcastInstanceByName(ContextUtil.id);
        return ins.getCluster().getMembers().stream().map(x -> (ServerInfo) x.getAttributes().get(Constant.SERVER_INFO)).collect(Collectors.toList());
    }


    /**
     * 随机一个服务
     */
    public static ServerInfo randomServerInfo(ServerTypeEnum serverType) {
        List<ServerInfo> list = getAllServerInfos().stream().filter(
                x -> x.getServerType() == serverType
        ).collect(Collectors.toList());
        return list.size() < 1 ? null : RandomUtil.randomEle(list);
    }

    public static List<ServerInfo> getServerInfosByType(ServerTypeEnum serverType) {
        return getAllServerInfos().stream().filter(
                x -> x.getServerType() == serverType
        ).collect(Collectors.toList());
    }

    public static ServerInfo getServerInfo(String serverId) {

        return getAllServerInfos().stream().filter(
                x -> x.getServerId().equals(serverId)
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

    public static void printServerStatus(ServerInfo serverInfo, boolean join) {
        String action = join ? "加入" : "离开";
        log.info(action + " 服务器 serverType= {} , serverId= {}", serverInfo.getServerId(), serverInfo.getServerType());
        log.info("当前所有的服务器={}", getAllServerInfos());
    }

}
