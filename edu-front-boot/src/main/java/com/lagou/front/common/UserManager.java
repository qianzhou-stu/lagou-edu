package com.lagou.front.common;

import com.lagou.common.util.UserContextHolder;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.Map;


/**
 * @ClassName UserManager
 * @Description UserManager
 * @Author zhouqian
 * @Date 2022/4/6 11:16
 * @Version 1.0
 */

public class UserManager {
    public static final String X_USER_ID = "x-user-id";
    public static final String X_USER_NAME = "x-user-name";
    public static final String X_USER_IP = "x-user-ip";

    /**
     * 获取用户id
     *
     * @Return String
     */
    public static Integer getUserId() {
        String userId = getItem(X_USER_ID);
        if (StringUtils.isNotBlank(userId) && NumberUtils.isNumber(userId)) {
            return Integer.parseInt(userId);
        }
        return -1;
    }

    private static String getItem(String itemName) {
        UserContextHolder instance = new UserContextHolder();
        if (null == instance) {
            return null;
        }
        Map<String, String> context = instance.getContext();
        if (MapUtils.isEmpty(context)) {
            return null;
        }
        return context.get(itemName);
    }

    /**
     * 获取用户名称
     *
     * @return String
     */
    public static String getUserName() {
        String userName = getItem(X_USER_NAME);
        if (StringUtils.isNotBlank(userName)) {
            return userName;
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取用户的IP地址
     *
     * @Return String
     */
    public static String getUserIP() {
        String userIP = getItem(X_USER_IP);
        if (StringUtils.isNotBlank(userIP)) {
            return userIP;
        }
        return StringUtils.EMPTY;
    }
}
