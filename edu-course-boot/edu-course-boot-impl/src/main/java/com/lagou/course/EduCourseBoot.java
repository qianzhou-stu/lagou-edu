package com.lagou.course;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
/**
 * @ClassName EduCourseBoot
 * @Description EduCourseBoot启动类
 * @Author zhouqian
 * @Date 2022/6/7 20:25
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan("com.lagou.course.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.lagou.course.api", "com.lagou.order.api"})
@Slf4j
public class EduCourseBoot {
    public static void main(String[] args) {
        SpringApplication.run(EduCourseBoot.class, args);
        System.out.println("start......");
    }
}
