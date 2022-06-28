package com.lagou.bom.config;

import com.lagou.bom.interceptor.GlobalInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class InterceptorConfig extends WebMvcConfigurerAdapter {
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new GlobalInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/user/login");
    }
}
