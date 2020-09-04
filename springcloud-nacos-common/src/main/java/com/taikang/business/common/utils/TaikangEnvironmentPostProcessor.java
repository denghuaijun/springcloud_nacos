package com.taikang.business.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

/**
 * @创建人 libin
 * @时间 2018/8/6
 * @描述
 */
@Slf4j
public class TaikangEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String confPath = System.getProperty(ConfigPropertiesLoader.CONF_JVM_ROOT_PATH);
        String confSystemPath = System.getProperty(ConfigPropertiesLoader.CONF_JVM_SYSTEM_ROOT_PATH);
        log.info("confPath : {} confSystemPath :{}",confPath,confSystemPath);
        ConfigPropertiesLoader configPropertiesLoader = ConfigPropertiesLoader.loaderInstance(confPath, confSystemPath);
        PropertiesPropertySource propertiesPropertySource = new PropertiesPropertySource("tk", configPropertiesLoader.getBusinessProperties());
        environment.getPropertySources().addLast(propertiesPropertySource);
    }

}
