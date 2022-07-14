package com.lagou.boss.interceptor;

import com.lagou.boss.common.UserManager;
import com.lagou.common.util.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GlobalInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String userId = request.getHeader(UserManager.X_USER_ID);
            String userName = request.getHeader(UserManager.X_USER_NAME);
            String userIp = request.getHeader(UserManager.X_USER_IP);

            Map<String, String> params = new HashMap<String, String>();
            params.put(UserManager.X_USER_ID, userId);
            params.put(UserManager.X_USER_IP, userIp);
            params.put(UserManager.X_USER_NAME, userName);

            UserContextHolder.getInstance().setContext(params);
            log.info("get userId:{}, userName:{} from header", userId, userName);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextHolder instance = UserContextHolder.getInstance();
        if (instance != null){
            Map<String, String> context = instance.getContext();
            if (MapUtils.isNotEmpty(context)){
                context.clear();
            }
        }
    }
}
