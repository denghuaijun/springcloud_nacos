package com.taikang.business.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by baoznst on 2018/8/3.
 */
public class NetUtils {
    private static final Logger logger = LoggerFactory.getLogger(NetUtils.class);

    /**
     * 获取本机ip
     *
     * @return
     */
    public static String getIp(){
        String ip = "";
        try {
            try {
                ip = InetAddress.getLocalHost().getHostAddress().toString();
            } catch (Exception e) {
            }
            if (StringUtils.isEmpty(ip)) {
                ip = toGetLocalLinkIP();
            }
        } catch (Exception e) {
        }
        return ip;
    }


    public static String toGetLocalLinkIP() throws Exception {
        String ip = "";

        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        while (en.hasMoreElements()) {
            NetworkInterface ni = en.nextElement();
            if (null != ni.getDisplayName() && ni.isUp()) {
                List<InterfaceAddress> list = ni.getInterfaceAddresses();
                Iterator<InterfaceAddress> it = list.iterator();
                while (it.hasNext()) {
                    InterfaceAddress ia = it.next();
                    if (null != ia.getBroadcast()) {
                        ip = ia.getAddress().getHostAddress();
                        if (!"127.0.0.1".equals(ip)) {
                            return ip;
                        }
                    }
                }
            }
        }
        return ip;
    }

    /**
     * 获取服务端口号
     *
     * @return
     * @throws Exception
     */
    public static String getPort() {
        String portStr = "";
        try {
            MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
            Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
                    Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
            String port = objectNames.iterator().next().getKeyProperty("port");
            if (StringUtils.isNotEmpty(port)) {
                portStr = port;
            }
            return portStr;
        } catch (Exception e) {
            logger.error("获取端口异常!");
        }
        return "";
    }
}
