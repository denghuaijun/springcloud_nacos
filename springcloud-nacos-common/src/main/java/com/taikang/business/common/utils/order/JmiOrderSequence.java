package com.taikang.business.common.utils.order;

import com.taikang.business.common.utils.NetUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Dept：订单全局ID生成类
 *
 * @author itw_denghj
 * Date:2018/9/27
 * Version:1.0
 */
public class JmiOrderSequence {
    private static final Logger logger = LoggerFactory.getLogger(JmiOrderSequence.class);
    private static Integer globalIndex = 1;
    private static Map<String, String> hostMap = new HashMap<>();
    private String hostConfigInfo;
    private Integer jvmIndex = -1;

    public JmiOrderSequence(String hostConfigInfo) {
        this.hostConfigInfo = hostConfigInfo;
        init();
    }

    public Long getOrderSequence() {
        return JmiOrderUtils.generateOrderId(jvmIndex);
    }

    /**
     * @desc 初始化解析配置文件的IP:机器编码，并将其放在静态变量map中
     */
    public void getHostConfigInfoList() {
        Map<String, String> map = new HashMap<>();
        String[] hosts = null;
        logger.info("机器的配置信息为hostConfigInfo:{},hostIndex:{}", hostConfigInfo, jvmIndex);
        if (StringUtils.isEmpty(hostConfigInfo)) {
            throw new IllegalArgumentException("本机IP端口对应机器配置信息propoerties为空！");
        }
        if (hostConfigInfo.contains(",")) {
            hosts = hostConfigInfo.split(",");
        } else {
            hosts = new String[]{hostConfigInfo};
        }
        if (hosts == null) {
            throw new IllegalArgumentException("未配置机器实例集合，请配置后启动！");
        }
        for (int i = 0; i < hosts.length; i++) {
            String[] str = hosts[i].split(":");
            if (str != null && str.length > 0) {
                map.put(str[0], str[1]);
            }
        }
        if (!map.isEmpty() && map.size() > 0) {
            hostMap = map;
        }
    }

    public void init() {
        //第一次解析配置文件时走此方法，后续不需走此方法进行解析
        if (hostMap.isEmpty()) {
            getHostConfigInfoList();
        }
        if (jvmIndex > 0) {
            return;
        }
        //获取当前机器IP_port
        String currentIP_Port = NetUtils.getIp() + "_" + NetUtils.getPort();
        if (StringUtils.isBlank(currentIP_Port)) {
            throw new IllegalArgumentException("获取本机IP端口号异常！");
        }
        for (Map.Entry<String, String> entry : hostMap.entrySet()) {
            if (currentIP_Port.equals(entry.getKey())) {
                this.jvmIndex = globalIndex + Integer.parseInt(entry.getValue().trim());
                break;
            }
        }
        if (jvmIndex == -1) {
            throw new IllegalArgumentException("配置文件中未找到本机IP端口对应的机器编码配置，请重新配置！本机IP端口为:" + currentIP_Port);
        }
        logger.info("计算后获取的机器序号加1为：{}", jvmIndex);
    }

    public void setHostConfigInfo(String hostConfigInfo) {
        this.hostConfigInfo = hostConfigInfo;
    }

    public String getHostConfigInfo() {
        return hostConfigInfo;
    }

    public Integer getJvmIndex() {
        return jvmIndex;
    }

    public void setJvmIndex(Integer jvmIndex) {
        this.jvmIndex = jvmIndex;
    }

}
