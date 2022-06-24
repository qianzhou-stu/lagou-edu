package com.lagou.bom;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Slf4j
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.lagou"})
@EnableCreateCacheAnnotation
public class EduBomBoot {
    public static void main(String[] args) {
        SpringApplication.run(EduBomBoot.class, args);
    }
}
