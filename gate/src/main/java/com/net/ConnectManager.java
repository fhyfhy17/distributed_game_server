package com.net;

import com.enums.GroupEnum;
import com.enums.ServerTypeEnum;
import com.hanlder.MessageGroup;
import com.hanlder.MessageThreadHandler;
import com.manager.ServerManager;
import com.net.handler.GateMessageHandler;
import com.pojo.Message;
import com.pojo.NettyMessage;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ConnectManager {

    public static AttributeKey<Session> USER_ID_KEY = AttributeKey.valueOf("userId");

    private final ConcurrentHashMap<String, Session> idToSessionMap = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Session> userIdToConnectMap = new ConcurrentHashMap<>();

    private MessageGroup m;
    @Autowired
    private NettyMessageFilter nettyMessageFilter;

    @PostConstruct
    public void startup() {
        m = new MessageGroup(GroupEnum.GATE_GROUP.name()) {
            @Override
            public MessageThreadHandler getMessageThreadHandler() {
                return new GateMessageHandler();
            }
        };
        m.startup();
    }

    public Session initConnect(Channel channel) {
        //连接注册，绑定session到channel上，可以通过链路 channel取得对应的session信息
        String sessionId = UUID.randomUUID().toString();

        Session session = new Session();
        session.setId(sessionId);
        session.setChannel(channel);

        channel.attr(USER_ID_KEY).set(session);

        idToSessionMap.put(sessionId, session);

        return session;
    }


    //返回登录成功了再注册,多点登录放在login服吧
    public Session register(String sessionId, String uid) {
        //登录注册，保存uid和session到userIdToConnectMap
        Session session = idToSessionMap.get(sessionId);
        session.setUid(uid);
        //绑定一个game服务器
        //保存gameId信息
//        ConnectUser connectUser = ConnectUserManger.getConnectUser(uid);
        String gameId = ServerManager.hashChooseServer(uid, ServerTypeEnum.GAME);
//        connectUser.setGameId(gameId);
        session.setGameId(gameId);
//        ConnectUserManger.saveConnectUser(connectUser);
        this.userIdToConnectMap.put(uid, session);
        return session;
    }

    //TODO 这得修整 ，变成正式的
    public void removeConnect(Channel channel) {
        //玩家断线调用，清除缓存，由于多端问题，通过区分不同的sessionId进行删除
        if (channel == null) {
            return;
        }
        Session session = channel.attr(USER_ID_KEY).get();
        if (session == null) {
            return;
        }
        this.idToSessionMap.remove(session.getId());
        if (session.getUid() == null) {
            return;
        }
        Session sessionNow = this.userIdToConnectMap.get(session.getUid());
        if (sessionNow.getId() == session.getId()) {
            this.userIdToConnectMap.remove(session.getUid());
        }

    }

    public void writeToClient(String uid, Message message) {
        Session session = userIdToConnectMap.get(uid);
        if (session != null) {
            session.writeMsg(message);
        }
    }

    /**
     * 包检测
     */
    long start;
    public void checkMessage(Session session, NettyMessage message) {
        if(session.getAutoIncrease()==5){
            start=System.currentTimeMillis();
        }
        if(session.getAutoIncrease()==1000000){
            log.info("完成，共用时 ={}",System.currentTimeMillis()-start);
        }
        if (!Objects.isNull(nettyMessageFilter)) {
            // 重复包检测
            if (!nettyMessageFilter.checkAutoIncrease(session, message)) {
                return;
            }

            // 篡改包检测
            if (!nettyMessageFilter.checkCode(session, message)) {
                return;
            }
        }

        // 解密  //TODO 加解密甚是爽朗
//        if (session.getPacketEncrypt().isEncrypt()) {
//            session.getPacketEncrypt().decode(packet.getByteArray(), packet.getIncode());
//        }
        addMessage(message);
    }

    public void addMessage(NettyMessage message) {
        m.messageReceived(message);
    }

    /**
     * 多点登录检测
     *
     * @param uid
     * @return
     */
    //TODO 待写
    private boolean loginMultipleCheck(String uid, String sessionId) {
        if (userIdToConnectMap.containsKey(uid)) {
            Session session = userIdToConnectMap.get(uid);
            if (session != null) {
                removeConnect(session.getChannel());
                session.getChannel().close();
                return true;
            }
        }
        return false;
    }

    public ConcurrentHashMap<String, Session> getIdToSessionMap() {
        return idToSessionMap;
    }

    public ConcurrentHashMap<String, Session> getUserIdToConnectMap() {
        return userIdToConnectMap;
    }
}
