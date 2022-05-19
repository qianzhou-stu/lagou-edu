package com.lagou.common.util;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Optional;

/**
 * @ClassName UserContextHolder
 * @Description 用户上下文
 * @Author zhouqian
 * @Date 2022/4/6 11:45
 * @Version 1.0
 */

public class UserContextHolder {
    private ThreadLocal<Map<String,String>> threadLocal;

    public UserContextHolder(){
        this.threadLocal = new ThreadLocal<>();
    }

    /**
     * 创建实例
     */
    public static UserContextHolder getInstance(){
        return SingletonHolder.sInstance;
    }

    /**
     * 静态内部类单例模式
     */
    private static class SingletonHolder{
        private static final UserContextHolder sInstance = new UserContextHolder();
    }
    /**
     * 获取上下文中放入信息
     * @Param map
     */
    public void setContext(Map<String,String> map){
        threadLocal.set(map);
    }
    /**
     * 获取上下文中的信息
     *
     * @return
     */
    public Map<String, String> getContext() {
        return threadLocal.get();
    }

    /**
     * 获取上下文中的用户名
     *
     * @return
     */
    public String getUsername() {
        return Optional.ofNullable(threadLocal.get()).orElse(Maps.newHashMap()).get("user_name");
    }
    /**
     * 清空上下文
     */
    public void clear() {
        threadLocal.remove();
    }

}
