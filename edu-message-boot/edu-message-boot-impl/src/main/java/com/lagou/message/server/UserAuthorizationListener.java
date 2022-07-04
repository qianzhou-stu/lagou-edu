package com.lagou.message.server;
import com.lagou.common.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import com.lagou.message.util.ServerConfigUtils;

import java.util.List;
import java.util.Map;

@Slf4j
public class UserAuthorizationListener implements AuthorizationListener {
    @Override
    public boolean isAuthorized(HandshakeData data) {
        JwtUtil.JwtResult userInfo = getUserInfo(data);
        if (userInfo == null || userInfo.getUserId() == null){
            String uri = data.getSingleUrlParam("uri");
            log.error("auth failed. url: {}", uri);
            return false;
        } else {
            log.info("auth success userId: {} userName: {}", userInfo.getUserId(), userInfo.getUserName());
            return true;
        }
    }

    public static JwtUtil.JwtResult getUserInfo(HandshakeData data) {
        Map<String, List<String>> params = data.getUrlParams();
        String authentication = params.get(HttpHeaders.AUTHORIZATION).get(0);
        if(StringUtils.isBlank(authentication)) {
            return null;
        }
        return JwtUtil.parse(ServerConfigUtils.instance.getSigningKey(), authentication);
    }
}
