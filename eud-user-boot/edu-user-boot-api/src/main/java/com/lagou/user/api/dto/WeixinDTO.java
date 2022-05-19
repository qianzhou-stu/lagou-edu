package com.lagou.user.api.dto;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName WeixinDTO
 * @Description WeixinDTO
 * @Author zhouqian
 * @Date 2022/4/8 19:14
 * @Version 1.0
 */
@Data
public class WeixinDTO {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 认证id,微信对应的时unionId
     */
    private String unionId;
    /**
     * openId
     */
    private String openId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 城市
     */
    private String city;
    /**
     * 性别：1-男，2-女
     */
    private Integer sex;
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
    private Boolean isDel;

}
