package com.lagou.order;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName EduOrderBoot
 * @Description EduOrderBoot启动类  订单服务启动类
 * @Author zhouqian
 * @Date 2022/6/8 16:17
 * @Version 1.0
 */
@SpringBootApplication(exclude = {
        DruidDataSourceAutoConfigure.class,
        DataSourceAutoConfiguration.class,
        MybatisPlusAutoConfiguration.class
})
@EnableConfigurationProperties
@EnableAutoDataSourceProxy
@MapperScan("com.lagou.order.mapper")
@EnableDiscoveryClient
@ComponentScan("com.lagou")
@EnableFeignClients(basePackages = {"com.lagou.course.api"})
@Slf4j
public class EduOrderBoot implements DisposableBean {

    private static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        ctx = SpringApplication.run(EduOrderBoot.class, args);
        for (String activeProfile : ctx.getEnvironment().getActiveProfiles()) {
            log.info(activeProfile);
        }
        log.info("spring cloud EduOrderBoot started!");
    }

    @Override
    public void destroy() throws Exception {
        if (null != ctx && ctx.isRunning()){
            ctx.close();
        }
    }
}
