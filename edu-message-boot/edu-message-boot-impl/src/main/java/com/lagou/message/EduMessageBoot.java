package com.lagou.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EduMessageBoot {
    public static void main(String[] args) {
        SpringApplication.run(EduMessageBoot.class, args);
        System.out.println("message starting ......");
    }
}
