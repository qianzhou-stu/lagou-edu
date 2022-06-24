package com.xxl.job.executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.xxl.job.executor", "com.xxl.job.executor.feign"})
public class XxlJobExecutorApplication {
    public static void main(String[] args) {
        SpringApplication.run(XxlJobExecutorApplication.class, args);
    }
}