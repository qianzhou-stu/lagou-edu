package com.lagou.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import javax.swing.*;

/**
 * @ClassName EduEurekaBoot
 * @Description EduEurekaBoot启动类
 * @Author zhouqian
 * @Date 2022/4/3 19:42
 * @Version 1.0
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaServer
public class EduEurekaBoot {
    public static void main(String[] args) {
        SpringApplication.run(EduEurekaBoot.class, args);
    }
}
