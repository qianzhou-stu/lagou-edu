package com.lagou.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @ClassName EduUserBoot
 * @Description EduUserBoot启动类
 * @Author zhouqian
 * @Date 2022/4/5 09:05
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.lagou.user.mapper")
public class EduUserBoot {
    public static void main(String[] args) {
        SpringApplication.run(EduUserBoot.class, args);
        System.out.println("start......");
    }
}
