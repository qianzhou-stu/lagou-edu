package com.lagou.common.response;


public enum EduEnum {

    PHONE_EXISTS(1001, "手机号已注册"),
    INSERT_FAILURE(1002, "创建失败"),
    UPDATE_FAILURE(1003, "更新失败"),
    IS_UPDATE_PASSWORD(1004, "是否更新密码"),
    UPDATE_PASSWORD_FAILURE(1005, "更新密码失败"),
    FORBID_USER_FAILURE(1006, "禁用用户"),
    ERROR_PHONE(1007, "非法手机号"),
    NOTNULL_PASSWORD_CODE(1008, "密码或者验证码为空"),
    ERROR_PHONE_PASSWORD(1009, "手机号或者密码错误"),
    NOTNULL_PHONE(1010, "手机号不能为空"),
    ISNULL_REFRESH_TOKEN(1011,"refresh_token为空"),
    LOGIN_FAILURE(1012,"登录失败"),
    ERROR_CODE(1013,"验证码错误"),
    INSERT_OR_UPDATE_FAILURE(1014, "创建或者更新失败"),
    ;
    private Integer code;
    private String msg;

    EduEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
