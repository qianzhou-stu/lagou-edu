package com.lagou.comment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class EduCommentBoot {
    @Autowired
    private MongoTemplate mongoTemplate;
    public static void main(String[] args) {
        SpringApplication.run(EduCommentBoot.class, args);
        System.out.println("start....");
    }
}
