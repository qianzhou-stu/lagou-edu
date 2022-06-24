package com.lagou.bom.user.service;

import com.lagou.bom.user.service.impl.OAuthRemoteServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// @FeignClient(name = "${remote.feign.edu-oauth-boot.name:edu-oauth-boot}", fallback = OAuthRemoteServiceFallback.class)
@FeignClient(name = "edu-bom-boot-oAuthRemoteService", fallback = OAuthRemoteServiceFallback.class)
public interface OAuthRemoteService {

    /**
     * 获取token的值
     * @param phone 手机号
     * @param password 密码
     * @param scope 授权范围
     * @param grantType 授权类型
     * @param clientId 客户端id
     * @param clientSecret 客户端密钥
     * @param authType 权限类型
     * @return String
     */
    @PostMapping("/oauth/token")
    String createToken(@RequestParam("username") String phone,
                       @RequestParam("password") String password,
                       @RequestParam("scope") String scope,
                       @RequestParam("grant_type") String grantType,
                       @RequestParam("client_id") String clientId,
                       @RequestParam("client_secret") String clientSecret,
                       @RequestParam(value = "auth_type", required = false) String authType);

    /**
     * 刷新token值
     * @param refreshToken 刷新token值
     * @param grantType 授权类型
     * @param clientId 客户端id
     * @param clientSecret 客户端密钥
     * @return String
     */
    @PostMapping("/oauth/token")
    String refreshToken(@RequestParam("refresh_token") String refreshToken,
                        @RequestParam("grant_type") String grantType,
                        @RequestParam("client_id") String clientId,
                        @RequestParam("client_secret") String clientSecret);
}
