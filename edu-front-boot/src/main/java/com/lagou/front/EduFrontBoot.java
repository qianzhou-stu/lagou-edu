package com.lagou.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName EduFrontBoot
 * @Description EduFrontBoot
 * @Author zhouqian
 * @Date 2022/4/4 12:35
 * @Version 1.0
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients(value = {"com.lagou.ad.api","com.lagou.user.api","com.lagou.oauth.api"})
public class EduFrontBoot {
    public static void main(String[] args) {
        SpringApplication.run(EduFrontBoot.class, args);
    }
}
