package com.taikang.business.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.util.*;

/**
 * Created by libin on 2018/4/4.
 * 配置文件读取
 */
@Slf4j
public class ConfigPropertiesLoader {

    /**
     * JVM配置的配置文件路径参数
     */
    public static final String CONF_JVM_ROOT_PATH = "conf_root_path";

    /**
     * 在系统环境变量里配置的配置文件路径参数
     */
    public static final String CONF_SYS_ROOT_PATH = "CONF_ROOT_PATH";


    /**
     * 配置的系统配置文件路径参数
     */
    public static final String CONF_JVM_SYSTEM_ROOT_PATH = "conf_system_root_path";

    /**
     * 在系统环境变量里配置的配置文件路径参数
     */
    public static final String CONF_SYS_SYSTEM_ROOT_PATH = "CONF_SYSTEM_ROOT_PATH";

    /**
     * 默认WINDOWS系统环境配置路径
     */
    public static final String DEFAULT_CONF_PATH_WINOS = "d:\\taikang\\confs\\env";

    /**
     * 默认的linux系统环境配置路径
     */
    public static final String DEFAULT_CONF_PATH_LINUXOS = "/home/tomcat/conf/env";


    /**
     * 默认WINDOWS系统公共配置路径
     */
    public static final String DEFAULT_CONF_SYSTEM_PATH_WINOS = "d:\\taikang\\confs\\system";

    /**
     * 默认的linux系统公共配置路径
     */
    public static final String DEFAULT_CONF_SYSTEM_PATH_LINUXOS = "/home/tomcat/conf/system";

    /**
     * xml 文件后缀
     */
    public static final String XML_FILE_EXTENSION = ".xml";


    /**
     * 环境文件配置变量
     */
    private String confRootPath;

    /**
     * 系统文件配置变量
     */
    private String confSystemRootPath;

    private Properties properties;

    private Map<String, Properties> envPropertiesMap;


    private Map<String, Properties> systemPropertiesMap;


    /**
     * 配置文件目录
     */
    private String envConfigDir;

    private String systemConfigDir;





    private static ConfigPropertiesLoader configPropertiesLoader;


    ConfigPropertiesLoader() {
        this(null,null);
    }

    public static ConfigPropertiesLoader loaderInstance(String confPath, String confSystemRootPath) {
        if (configPropertiesLoader == null) {
            if (StringUtils.isNotBlank(confPath)) {
                configPropertiesLoader = new ConfigPropertiesLoader(confPath,confSystemRootPath);

            } else {
                configPropertiesLoader = new ConfigPropertiesLoader();
            }

            configPropertiesLoader.loadConfigFilesProperties();
        }
        return configPropertiesLoader;
    }

    /**
     * 读取配置文件
     * @param env
     * @param fileName
     * @return
     */
    public Properties getPropertiesMap(String env, String fileName) {
        if (env.equals("env")) {
            return envPropertiesMap.get(fileName);
        } else if (env.equals("system")) {
            return systemPropertiesMap.get(fileName);
        }
        throw new RuntimeException("未找到配置文件" + fileName);
    }

    /**
     * 读取环境配置文件路径
     * @return
     */
    public String getEnvConfigDir() {
        return envConfigDir;
    }

    /**
     *系统配置文件路径
     * @return
     */
    public String getSystemConfigDir() {
        return systemConfigDir;
    }

    /**
     * 合并后的全部的配置文件
     * @return
     */
    public Properties getBusinessProperties() {
        return this.properties;
    }

    /**
     * 设置参数路径变量
     * @param confRootPath
     * @param confSystemRootPath
     */
    public ConfigPropertiesLoader(String confRootPath, String confSystemRootPath) {
        this.systemPropertiesMap = new HashMap<>();
        this.envPropertiesMap = new HashMap<>();
        this.properties = new Properties();
        //未设置confRootPath参数使用默认配置
        if (StringUtils.isBlank(confRootPath)) {
            //读取JVM参数变量
            this.confRootPath  = System.getProperty(CONF_JVM_ROOT_PATH);
            if (StringUtils.isBlank(confRootPath)) {
                //读取环境变量
                this.confRootPath  = System.getenv(CONF_SYS_ROOT_PATH);
            }

            //根据操作系统读取默认变量
            if (StringUtils.isBlank(confRootPath)) {
                //查询项目盘符
                if (SystemUtils.IS_OS_WINDOWS) {
                    this.confRootPath  = DEFAULT_CONF_PATH_WINOS;
                } else if (SystemUtils.IS_OS_LINUX) {
                    this.confRootPath  = DEFAULT_CONF_PATH_LINUXOS;
                } else if (SystemUtils.IS_OS_MAC) {
                    this.confRootPath  = DEFAULT_CONF_PATH_LINUXOS;
                }
            }
        }else{
            this.confRootPath  = confRootPath;
        }
        //未设置系统配置时使用默认配置
        if (StringUtils.isBlank(confSystemRootPath)) {
            //读取JVM参数变量
            this.confSystemRootPath  = System.getProperty(CONF_JVM_SYSTEM_ROOT_PATH);
            if (StringUtils.isBlank(confSystemRootPath)) {
                //读取环境变量
                this.confSystemRootPath = System.getenv(CONF_SYS_SYSTEM_ROOT_PATH);
            }

            //根据操作系统读取默认变量
            if (StringUtils.isBlank(confSystemRootPath)) {
                //查询项目盘符
                if (SystemUtils.IS_OS_WINDOWS) {
                    this.confSystemRootPath = DEFAULT_CONF_SYSTEM_PATH_WINOS;
                } else if (SystemUtils.IS_OS_LINUX) {
                    this.confSystemRootPath = DEFAULT_CONF_SYSTEM_PATH_LINUXOS;
                } else if (SystemUtils.IS_OS_MAC) {
                    this.confSystemRootPath = DEFAULT_CONF_SYSTEM_PATH_LINUXOS;
                }
            }
        }else{
            this.confSystemRootPath = confSystemRootPath;
        }


    }


    /**
     * 加载配置文件
     */
    private void loadConfigFilesProperties() {
        if (confRootPath != null) {
            File configDir = new File(confRootPath);
            if (configDir.exists()) {
                loadPropertiesFromPath(configDir, "env");
            } else {
                log.error("配置文件路径不存在 " + confRootPath + " 或者配置JVM环境参数 例如conf_root_path=/confs/ ");
            }

            File systemDir = new File(confSystemRootPath);
            if (systemDir.exists()) {
                loadPropertiesFromPath(systemDir, "system");
            } else {
                log.error("配置文件路径不存在 " + confRootPath + " 或者配置JVM环境参数 例如conf_system_root_path=/confs/ ");
            }

            //配置文件路径
            try {
                envConfigDir = configDir.getCanonicalPath();
                systemConfigDir = systemDir.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            throw new NullPointerException();
        }
    }



    private void loadPropertiesFromPath(File dir, String env) {
        if (dir == null) {
            return;
        }
        if (!dir.exists()) {
            return;
        }
        if (!dir.isDirectory()) {
            return;
        }
        if (dir.isHidden() || !dir.canRead()) {
            return;
        }
        this.loadPropertiesFromDir(dir, env);

    }


    private LinkedList<File> traverseFolder(File path) {
        LinkedList<File> configFile = new LinkedList<>();
        LinkedList<File> list = new LinkedList();
        if (path != null && path.exists()) {
            File[] files = path.listFiles(new ConfigPropertiesFileFilter(true));
            for (File file : files) {
                if (file.isDirectory()) {
                    list.add(file);
                } else {
                    configFile.add(file);
                }
            }
            File tempFile;
            while (!list.isEmpty()) {
                tempFile = list.removeFirst();
                files = tempFile.listFiles(new ConfigPropertiesFileFilter(true));
                for (File file : files) {
                    if (file.isDirectory()) {
                        list.add(file);
                    } else {
                        configFile.add(file);
                    }
                }
            }
        }
        return configFile;
    }

    private void loadPropertiesFromDir(File dirRoot, String env) {
        LinkedList<File> files = traverseFolder(dirRoot);
        for (File file : files) {
            loadPropertiesFromFile(file, env);
        }

    }





    private void loadPropertiesFromFile(File file, String env) {
        Properties p = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            if (file.getName().endsWith(XML_FILE_EXTENSION)) {
                p.loadFromXML(in);
            } else {
                p.load(in);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }

        if (env.equals("env")) {
            if (envPropertiesMap.get(file.getName()) != null) {
                throw new RuntimeException("全局配置文件不能包含相同文件名的配置文件 " + file.getName());
            }
            envPropertiesMap.put(file.getName(), p);
        } else if (env.equals("system")) {
            if (systemPropertiesMap.get(file.getName()) != null) {
                throw new RuntimeException("全局配置文件不能包含相同文件名的配置文件 " + file.getName());
            }
            systemPropertiesMap.put(file.getName(), p);
        }
        mergeProperties(file, p, this.properties);
    }


    /**
     * 合并配置
     *
     * @param properties
     */
    private void mergeProperties(Properties properties) {
        mergeProperties(null, properties, this.properties);
    }

    /**
     * 配置文件合并重复设置检测
     * @param file
     * @param from
     * @param to
     */
    private static void mergeProperties(File file, Properties from, Properties to) {
        for (Enumeration en = from.propertyNames(); en.hasMoreElements(); ) {
            String key = (String) en.nextElement();

            if (StringUtils.isBlank(key)) {
                continue;
            }
            Object value = System.getProperty(key);
            if (value == null) {
                value = from.getProperty(key);
                if (value == null) {
                    value = from.get(key);
                }
            }

            Object findOld = to.put(key, value == null ? "" : value.toString());

            if (findOld != null) {
                if (file != null) {
                    log.error(String.format("file : %s property key :%s new value(%s) override old value(%s) ", file.getAbsolutePath(), key, value, findOld));
                } else {
                    log.error(String.format("property key :%s new value(%s) override old value(%s) ", key, value, findOld));
                }

            }
        }
    }



    private static class ConfigPropertiesFileFilter implements FileFilter {

        private boolean inclodeDir;

        public ConfigPropertiesFileFilter(boolean inclodeDir) {
            this.inclodeDir = inclodeDir;
        }

        @Override
        public boolean accept(File file) {
            if (file.isHidden()) {
                return false;
            }
            if (!file.canRead()) {
                return false;
            }

            if (file.isDirectory()) {
                if (this.inclodeDir) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (file.getName().endsWith(".properties") || file.getName().endsWith(".xml")) {
                    return true;
                } else {
                    return false;
                }
            }
        }

    }
}
