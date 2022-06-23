package com.lagou.oauth.config;

import com.google.common.collect.Lists;
import com.lagou.oauth.exception.CustomWebResponseExceptionTranslator;
import com.lagou.oauth.mult.MultAuthenticationFilter;
import com.lagou.oauth.oauth2.enhancer.CustomTokenEnhancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * oauth2.0 配置的类
 */
@Slf4j
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;
    @Autowired
    @Qualifier("userDetailsService")
    UserDetailsService userDetailsService;
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
    @Autowired
    private MultAuthenticationFilter multAuthenticationFilter; // 自定义多认证过滤器
    /**
     * jwt 对称加密密钥
     */
    @Value("${spring.security.oauth2.jwt.signingKey}")
    private String signingKey;

    // 用来配置令牌端点的安全约束。 配置允许访问token的地址
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        // 支持将client参数放在header或body中
        oauthServer
                .tokenKeyAccess("permitAll()") // /oauth/token_key公开
                .checkTokenAccess("permitAll()") // /oauth/check_token公开
                .allowFormAuthenticationForClients() // 表单认证，申请令牌
//                .tokenEndpointAuthenticationFilters(null)
                .addTokenEndpointAuthenticationFilter(multAuthenticationFilter); // 添加token认证的多过滤器
    }

    // 配置客户端的详细信息服务
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置客户端信息，从数据库中读取，对应oauth_client_details表
        // 配置客户端的id，客户端的密钥，资源列表，授权类型，允许的授权范围，false 跳转到授权页面，加上验证回调地址
        clients.jdbc(dataSource);
    }

    // 用来配置令牌token的访问端点和令牌服务（token services）
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        // 配置token的数据源、自定义的tokenServices等信息,配置身份认证器，配置认证方式，TokenStore，TokenGranter，OAuth2RequestFactory
        // tokenStore(): token的持久化
        endpoints.tokenStore(tokenStore())
                //该字段设置设置refresh token是否重复使用,true:reuse;false:no reuse.
                .reuseRefreshTokens(false)
                .authorizationCodeServices(authorizationCodeServices()) // 授权码模式持久化授权码code
                .approvalStore(approvalStore()) // 授权信息持久化实现
                .exceptionTranslator(customExceptionTranslator()) // 自定义OAuth2异常处理
                .tokenEnhancer(tokenEnhancerChain()) // 自定义token增强链
                .authenticationManager(authenticationManager) // 权限管理器 任务管理器
                .userDetailsService(userDetailsService) // 用户细节服务
                .tokenGranter(tokenGranter(endpoints)); // 配置自定义的granter
    }

    /**
     * 自定义OAuth2异常处理
     */
    @Bean
    public WebResponseExceptionTranslator<OAuth2Exception> customExceptionTranslator() {
        return new CustomWebResponseExceptionTranslator();
    }

    /**
     * 授权信息持久化实现
     *
     * @return JdbcApprovalStore
     */
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * 授权码模式持久化授权码code
     */
    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        // 授权码存储等处理方式类，使用jdbc，操作oauth_code表
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * token的持久化
     */
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    /**
     * 自定义token增强链
     */
    @Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new CustomTokenEnhancer(), accessTokenConverter()));
        return tokenEnhancerChain;
    }

    /**
     * jwt token的生成配置
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signingKey);
        return converter;
    }

    /**
     * 配置自定义的granter
     */
    public TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> granters = Lists.newArrayList(endpoints.getTokenGranter());
        return new CompositeTokenGranter(granters);
    }

}
/**
 * oauth_access_token: 访问令牌表
 * oauth_approvals: 授权记录表
 * oauth_client_details: 客户端信息
 * oauth_client_token: 客户端授权令牌表
 * oauth_code: 授权码表
 * oauth_refresh_token: 更新令牌表
 * <p>
 * 授权码模式 ： 拿到授权码  然后再拿到token密钥
 * 简化模式： 直接拿到token密钥再url地址
 * 密码模式： 通过客户端的账号和密码获取token密钥
 * 客户端模式： 客户端必须是可信的客户端，会拿到token直接去后端找
 * 资源服务模式： 对资源的进行限制访问
 * refreshToken模式
 */

/**
 * 授权码模式 ： 拿到授权码  然后再拿到token密钥
 * 简化模式： 直接拿到token密钥再url地址
 * 密码模式： 通过客户端的账号和密码获取token密钥
 * 客户端模式： 客户端必须是可信的客户端，会拿到token直接去后端找
 * 资源服务模式： 对资源的进行限制访问
 * refreshToken模式
 */