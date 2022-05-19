package com.lagou.oauth.mult.authenticator.password;


import com.lagou.oauth.mult.MultAuthentication;
import com.lagou.oauth.mult.authenticator.AbstractPreparableMultAuthenticator;
import com.lagou.user.api.dto.UserDTO;
import com.lagou.user.api.feign.UserRemoteService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * 默认登录处理
 **/
@Component
@Primary
public class UsernamePasswordAuthenticator extends AbstractPreparableMultAuthenticator {

    @Autowired
    private UserRemoteService userRemoteService;

    @Override
    public UserDTO authenticate(MultAuthentication multAuthentication) {
        return this.userRemoteService.getUserByPhone(multAuthentication.getUsername());
    }

    @Override
    public void prepare(MultAuthentication multAuthentication, HttpServletResponse response) {

    }

    @Override
    public boolean support(MultAuthentication multAuthentication) {
        return StringUtils.isEmpty(multAuthentication.getAuthType());
    }
}
