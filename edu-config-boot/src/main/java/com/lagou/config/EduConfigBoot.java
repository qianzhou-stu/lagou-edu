package com.lagou.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @ClassName EduConfigBoot
 * @Description EduConfigBoot
 * @Author zhouqian
 * @Date 2022/4/3 19:57
 * @Version 1.0
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableConfigServer
@EnableDiscoveryClient
public class EduConfigBoot {
    public static void main(String[] args) {
        SpringApplication.run(EduConfigBoot.class, args);
    }
}
