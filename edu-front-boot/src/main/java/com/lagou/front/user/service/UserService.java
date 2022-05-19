package com.lagou.front.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lagou.oauth.api.feign.OAuthRemoteService;
import com.lagou.common.response.EduEnum;
import com.lagou.common.response.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author zhouqian
 * @Date 2022/4/6 19:27
 * @Version 1.0
 */
@Slf4j
@Service
@RefreshScope
public class UserService {
    @Autowired
    private OAuthRemoteService oAuthRemoteService;
    @Value("${spring.oauth.client_id}")
    private String clientId;
    @Value("${spring.oauth.client_secret}")
    private String clientSecret;
    @Value("${spring.oauth.scope}")
    private String scope;
    @Value("${spring.oauth.grant_type}")
    private String grantType;
    @Value("${spring.oauth.refresh_grant_type}")
    private String refreshGrantType;

    public ResponseDTO createAuthToken(String phone, String password, String code, Integer type) {
        log.info("phone:{}, password:{}, scope:{}, grantType:{}, clientId:{}, clientSecret:{}", phone, password, scope, grantType, clientId, clientSecret);
        String token = null;
        try {
            if (type == 0){
                token = this.oAuthRemoteService.createToken(phone, password, scope, grantType, clientId, clientSecret, null);
            } else if (1 == type) {
                token = this.oAuthRemoteService.createToken(phone, code, scope, grantType, clientId, clientSecret, "mobile");
            } else if (2 == type) {
                token = this.oAuthRemoteService.createToken(phone, code, scope, grantType, clientId, clientSecret, "weixin");
            }
            if (StringUtils.isBlank(token)) {
                return ResponseDTO.ofError(EduEnum.LOGIN_FAILURE.getCode(), EduEnum.LOGIN_FAILURE.getMsg());
            }
            JSONObject jsonObject = JSON.parseObject(token);
            String resultCode = jsonObject.getString("code");
            if (StringUtils.isNotBlank(resultCode)){
                log.info("phone:{}, jwt token is null, token:{}", phone, token);
                if ("040072".equals(resultCode)){
                    return ResponseDTO.ofError(EduEnum.ERROR_CODE.getCode(), EduEnum.ERROR_PHONE.getMsg() + "(" + resultCode + ")");
                }
                return ResponseDTO.ofError(EduEnum.LOGIN_FAILURE.getCode(), EduEnum.LOGIN_FAILURE.getMsg() + "(" + resultCode + ")");
            }
            String userId = jsonObject.getString("user_id");
            if (StringUtils.isBlank(userId)){
                return ResponseDTO.ofError(EduEnum.LOGIN_FAILURE.getCode(), EduEnum.LOGIN_FAILURE.getMsg());
            }
            return ResponseDTO.success(token);
        }catch (Exception e){
            log.error("", e);
            return ResponseDTO.ofError(EduEnum.LOGIN_FAILURE.getCode(), EduEnum.LOGIN_FAILURE.getMsg());
        }
    }

}
