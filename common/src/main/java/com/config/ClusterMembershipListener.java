package com.config;

import com.Constant;
import com.hazelcast.core.Member;
import com.hazelcast.core.MemberAttributeEvent;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;
import com.manager.ServerInfoManager;
import com.pojo.ServerInfo;

public class ClusterMembershipListener
        implements MembershipListener {

    public void memberAdded(MembershipEvent membershipEvent) {

        if (!membershipEvent.getMember().localMember()) {
            Member member = membershipEvent.getMember();
            ServerInfoManager.addServerInfo(
                    (ServerInfo) member.getAttributes().get(Constant.SERVER_INFO));

        }


    }

    public void memberRemoved(MembershipEvent membershipEvent) {
        if (!membershipEvent.getMember().localMember()) {
            Member member = membershipEvent.getMember();
            ServerInfoManager.removeServerInfo(
                    ((ServerInfo) member.getAttributes().get(Constant.SERVER_INFO)).getServerId());
        }
    }

    public void memberAttributeChanged(MemberAttributeEvent memberAttributeEvent) {
        System.err.println("Member attribute changed: " + memberAttributeEvent);
    }

}