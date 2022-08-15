package com.lagou.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * web服务安全 配置的类
 */
@Configuration
@EnableWebSecurity
public class WebServerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    /**
     * 配置可以访问的地址
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // 禁用csrf()
        http.httpBasic().disable(); // 禁用httpBasic() 基础http验证
        http.formLogin().disable(); // 禁用formLogin() 表单验证呢
        http.authorizeRequests().antMatchers("/actuator/**", "/oauth/token").permitAll().anyRequest().authenticated().and().logout().permitAll().and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

    /**
     * 注入自定义的userDetailsService实现，获取用户信息，设置密码加密方式
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * 释放静态资源的拦截
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        // 解决静态资源被拦截的问题
        web.ignoring().antMatchers("/css/**", "/image/**", "/js/**"); // 主要是用于释放静态资源的访问
    }

    /**
     * 将 AuthenticationManager 注册为 bean , 方便配置 oauth server 的时候使用
     * 将 AuthenticationMabager 注册为Bean，
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}