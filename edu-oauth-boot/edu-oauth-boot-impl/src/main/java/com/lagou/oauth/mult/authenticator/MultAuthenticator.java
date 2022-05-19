package com.lagou.oauth.mult.authenticator;



import com.lagou.oauth.mult.MultAuthentication;
import com.lagou.user.api.dto.UserDTO;

import javax.servlet.http.HttpServletResponse;

/**
 *
 **/
public interface MultAuthenticator {

    /**
     * 处理集成认证
     *
     * @param multAuthentication
     * @return
     */
    UserDTO authenticate(MultAuthentication multAuthentication);

    /**
     * 进行预处理
     *
     * @param multAuthentication
     * @param response
     */
    void prepare(MultAuthentication multAuthentication, HttpServletResponse response);

    /**
     * 判断是否支持集成认证类型
     *
     * @param multAuthentication
     * @return
     */
    boolean support(MultAuthentication multAuthentication);

    /**
     * 认证结束后执行
     *
     * @param multAuthentication
     */
    void complete(MultAuthentication multAuthentication);

}
