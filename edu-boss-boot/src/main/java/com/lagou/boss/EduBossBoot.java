package com.lagou.boss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName EduBossBoot
 * @Description TODO
 * @Author zhouqian
 * @Date 2022/4/4 10:34
 * @Version 1.0
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients("com.lagou.ad.api")
public class EduBossBoot {
    public static void main(String[] args) {
        SpringApplication.run(EduBossBoot.class, args);
    }
}
