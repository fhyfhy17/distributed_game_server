package com.config;

import com.Constant;
import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.TcpIpConfig;
import com.pojo.ServerInfo;
import com.util.ContextUtil;
import com.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@Slf4j
public class HazelCastConfig {

    @Autowired
    private ServerInfo serverInfo;


    @Bean(name = "hazelcast")
    public Config config() {
        ClasspathXmlConfig classpathXmlConfig = new ClasspathXmlConfig("cluster.xml");
        //本地服务名
        classpathXmlConfig.setInstanceName(ContextUtil.id);
        classpathXmlConfig.getMemberAttributeConfig().setAttributes(Collections.singletonMap(Constant.SERVER_INFO,serverInfo));
        //自身IP
        TcpIpConfig tcpIpConfig = classpathXmlConfig.getNetworkConfig().getJoin().getTcpIpConfig();
        tcpIpConfig.setEnabled(true);
        tcpIpConfig.addMember(IpUtil.getHostIp());
        //发现IP
        InterfacesConfig interfaces = classpathXmlConfig.getNetworkConfig().getInterfaces();

        interfaces.setEnabled(true);
        interfaces.addInterface("192.168.*.*");

        return classpathXmlConfig;
    }
}

