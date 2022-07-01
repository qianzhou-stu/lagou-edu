package com.lagou.gateway.filter;

import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@ComponentScan(basePackages = "com.lagou")
public class AccessGatewayFilter implements GlobalFilter {
    private final Logger log = LoggerFactory.getLogger(AccessGatewayFilter.class);
    // private final Logger log = LoggerFactory.getLogger(getClass());
    private static final String X_USER_ID = "x-user-id";
    private static final String X_USER_NAME = "x-user-name";
    private static final String X_USER_IP = "x-user-ip";
    private static final String BOSS_PATH_PREFIX = "/boss";

    /**
     * 由authentication-client模块提供鉴权的feign客户端
     */
   /* @Autowired
    private IAuthService authService;
    @Autowired
    private IPermissionService permissionService;*/

    /**
     * 1.首先网关检查token是否有效，无效直接返回401，不调用鉴权服务
     * 2.调用鉴权服务器看是否对该请求有权限，有权限进入下一个filter，没有权限返回403
     * <p> 注：前端会根据401去做登录跳转或者更新token。而无权限返回403
     * <p/>
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return null;
    }
}
