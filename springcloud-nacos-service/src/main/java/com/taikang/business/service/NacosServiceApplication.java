package com.taikang.business.service;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@ComponentScan(basePackages = {"com.taikang.business.service"})
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableAspectJAutoProxy
public class NacosServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosServiceApplication.class,args);
    }
}
