package com.lagou.comment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.lagou.edu.comment.api", "com.lagou.user.api"})
public class EduCommentBoot {
    @Autowired
    private MongoTemplate mongoTemplate;
    public static void main(String[] args) {
        SpringApplication.run(EduCommentBoot.class, args);
        System.out.println("start....");
    }
}
