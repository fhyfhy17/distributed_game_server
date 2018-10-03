package com;

import com.hazelcast.core.*;
import com.hazelcast.map.listener.EntryAddedListener;
import com.manager.ServerInfoManager;
import com.manager.VertxMessageManager;
import com.net.msg.Message;
import com.pojo.ServerInfo;
import com.util.SerializeUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Map;
import java.util.Set;


public abstract class BaseVerticle extends AbstractVerticle {
    final transient static Logger log = LoggerFactory.getLogger(BaseVerticle.class);


    @Override
    public void start() throws Exception {
        super.start();

        EventBus eventBus = vertx.eventBus();
        vertx.deployVerticle(VertxMessageManager.class, new DeploymentOptions().setWorker(true).setInstances(3));
        eventBus.consumer(getServerInfo().getServerId(), msg -> {
            getHandler().onReceive(SerializeUtil.toObj(msg.body().toString(),Message.class));
        });

        publishService();

    }

    protected void publishService() {
        // 发布服务信息

        HazelcastInstance ins = Hazelcast.getHazelcastInstanceByName(getServerInfo().getServerId());
        // 获取其他服务的信息缓存到本地
        IMap<String, ServerInfo> map = ins.getMap(Constant.SERVER_MAP);
        Set<Member> members = ins.getCluster().getMembers();
        for (Member member : members) {
            if (member.localMember()) {
                // 发布本节点
                map.put(member.getUuid(), getServerInfo());
            } else {
                // 读取其他节点
                Set<Map.Entry<String, ServerInfo>> entries = map.entrySet();
                for (Map.Entry<String, ServerInfo> entry : entries) {
                    ServerInfoManager.addServerInfo(entry.getValue());
                }

            }
        }
        map.addEntryListener((EntryAddedListener<String, ServerInfo>) event -> {
            // 监听其他节点加入
            ServerInfo value = event.getValue();
            ServerInfoManager.addServerInfo(value);
            System.out.println("来了一个节点" + value.getServerId());
        }, true);


        ins.getCluster().addMembershipListener(new MembershipListener() {

            @Override
            public void memberRemoved(MembershipEvent membershipEvent) {
                if (!membershipEvent.getMember().localMember()) {
                    Member member = membershipEvent.getMember();
                    log.info("集群中节点关闭！" + member);
                    ServerInfo remove = map.remove(membershipEvent.getMember().getUuid());
                    ServerInfoManager.removeServerInfo(remove.getServerId());
                }
            }

            @Override
            public void memberAttributeChanged(MemberAttributeEvent memberAttributeEvent) {
            }

            @Override
            public void memberAdded(MembershipEvent membershipEvent) {
//
//                ServerInfo value = event.getValue();
//                ServerInfoHelper.addServerInfo(value);
//                System.out.println("来了一个节点" + value.getServerId());
            }
        });

    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }


    public abstract BaseHandler getHandler();

    public abstract ServerInfo getServerInfo();
}
