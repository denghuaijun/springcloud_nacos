package com.taikang.business.common.utils;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class Env {
    private static String env = System.getProperty("env");

    public Env() {
    }

    public static boolean isTest() {
        return "TEST".equals(env) || "TEST".toLowerCase().equals(env);
    }

    public static boolean isDev() {
        return env == null || "DEV".equals(env) || "DEV".toLowerCase().equals(env);
    }

    public static boolean isProd() {
        return "PROD".equals(env) || "PROD".toLowerCase().equals(env);
    }

    public static String getEnv() {
        return isProd()?"PROD":(isTest()?"TEST":"DEV");
    }

    /**
     * 自定义配置日志显示log_mode=DEV/TEST/PROD（默认是env变量参数） 配置系统启动环境变量 -Denv=TEST或DEV或PROD   -Dconf_root_path=/conf/test 配置文件路径（环境相关）  -Dconf_system_root_path=/conf/test/system 配置文件路径（系统相关）
     * @throws IOException
     */
    public static void initLaunchVariable() throws IOException {
        Properties properties = PropertiesLoaderUtils.loadAllProperties("application.properties");
        //设置utf8
        System.setProperty("file.encoding", "UTF-8");
        //设置系统debug环境
        if (Env.isDev()) {
            System.setProperty("env.debug", "true");
        } else {
            System.setProperty("env.debug", "false");
        }
        //配置ENV环境和日志环境
        System.setProperty("spring.profiles.active", getEnv().toLowerCase());
        //配置Log级别 默认和env相关 如果配置logmode 就按照logmode显示日志
        String logMode = System.getProperty("log_mode");
        if (logMode != null) {
            System.setProperty("spring.profiles.active",logMode.toLowerCase());
        }
        //设置配置文件路径
        String confpathEnv = System.getProperty("conf_root_path");
        if (confpathEnv == null) {
            confpathEnv = properties.getProperty("conf_root_path");
            System.setProperty("conf_root_path",confpathEnv);
        }
        //设置系统文件路径
        String confpathSystem = System.getProperty("conf_system_root_path");
        if (confpathSystem == null) {
            confpathSystem = properties.getProperty("conf_system_root_path");
            System.setProperty("conf_system_root_path",confpathSystem);
        }




    }
}
