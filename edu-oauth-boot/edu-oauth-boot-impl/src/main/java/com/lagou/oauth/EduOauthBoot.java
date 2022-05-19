package com.lagou.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName EduOauthBoot
 * @Description EduOauthBoot
 * @Author zhouqian
 * @Date 2022/4/6 22:58
 * @Version 1.0
 */
@SpringBootApplication
@EnableFeignClients("com.lagou.user.api")
public class EduOauthBoot {
    public static void main(String[] args) {
        SpringApplication.run(EduOauthBoot.class, args);
    }
}
