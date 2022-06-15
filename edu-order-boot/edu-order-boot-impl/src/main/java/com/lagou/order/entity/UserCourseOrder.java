package com.lagou.order.entity;

import java.util.Date;

import io.swagger.models.auth.In;
import lombok.Data;

/**
* 用户课程订单表
*/
@Data
public class UserCourseOrder {
    /**
    * 主键
    */
    private Long id;

    /**
    * 订单号
    */
    private String orderNo;

    /**
    * 用户id
    */
    private Integer userId;

    /**
    * 课程id，根据订单中的课程类型来选择
    */
    private Integer courseId;

    /**
    * 活动课程id
    */
    private Integer activityCourseId;

    /**
    * 订单来源类型: 1 用户下单购买 2 后台添加专栏
    */
    private Integer sourceType;

    /**
    * 当前状态: 0已创建 10已支付 20已完成 30已取消 40已过期 
    */
    private Integer status;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
    * 是否删除
    */
    private Byte isDel;
}