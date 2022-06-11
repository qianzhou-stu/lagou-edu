package com.lagou.order.api.enums;

public enum StatusTypeEnum {
    INSERT("保存"),
    UPDATE("更新"),
    CANCEL("超时取消"),
    ;
    public final String desc;
    StatusTypeEnum(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
