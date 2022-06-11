package com.lagou.order.api.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName UserCourseOrderSourceType
 * @Description UserCourseOrderSourceType
 * @Author zhouqian
 * @Date 2022/6/8 11:21
 * @Version 1.0
 */

public enum UserCourseOrderSourceType {
    USER_BUY(1, "用户下单购买"),
    OFFLINE_BUY(2, "后台添加专栏"),
    ;
    private Integer code;
    private String name;
    UserCourseOrderSourceType(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    private static final Map<Integer, UserCourseOrderSourceType> CACHE = new HashMap<Integer, UserCourseOrderSourceType>();

    static {
        for (UserCourseOrderSourceType val : UserCourseOrderSourceType.values()){
            CACHE.put(val.getCode(), val);
        }
    }
    /**
     * 根据code值来转换为枚举类型
     */
    public static UserCourseOrderSourceType parse(Integer code){
        return CACHE.get(code);
    }

}
