package com.lagou.order.entity;

import java.util.Date;
import lombok.Data;

/**
* 课程订单状态日志表
*/
@Data
public class UserCourseOrderRecord {
    /**
    * 主键ID
    */
    private Integer id;

    /**
    * 订单号
    */
    private String orderNo;

    /**
    * 原订单状态
    */
    private String fromStatus;

    /**
    * 新订单状态
    */
    private String toStatus;

    /**
    * 备注
    */
    private String remark;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 创建人
    */
    private String createUser;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
    * 更新人
    */
    private String updateUser;
}