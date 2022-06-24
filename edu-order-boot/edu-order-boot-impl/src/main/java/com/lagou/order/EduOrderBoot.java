package com.lagou.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName EduOrderBoot
 * @Description EduOrderBoot启动类
 * @Author zhouqian
 * @Date 2022/6/8 16:17
 * @Version 1.0
 */
@SpringBootApplication
@MapperScan("com.lagou.order.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.lagou.course.api"})
public class EduOrderBoot {
    public static void main(String[] args) {
        SpringApplication.run(EduOrderBoot.class, args);
    }
}
