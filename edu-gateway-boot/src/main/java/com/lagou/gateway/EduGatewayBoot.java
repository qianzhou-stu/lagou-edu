package com.lagou.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName EduGatewayBoot
 * @Description EduGatewayBoot启动类
 * @Author zhouqian
 * @Date 2022/4/4 19:57
 * @Version 1.0
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class EduGatewayBoot {
    public static void main(String[] args) {
        SpringApplication.run(EduGatewayBoot.class, args);
    }
}
