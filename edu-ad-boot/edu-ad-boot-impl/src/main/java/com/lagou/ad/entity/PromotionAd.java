package com.lagou.ad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhouqian
 * @since 2022-04-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PromotionAd implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 广告名
     */
    @TableField("name")
    private String name;

    /**
     * 广告位id
     */
    @TableField("spaceId")
    private Integer spaceId;

    /**
     * 精确搜索关键词
     */
    @TableField("keyword")
    private String keyword;

    /**
     * 静态广告的内容
     */
    @TableField("htmlContent")
    private String htmlContent;

    /**
     * 文字
     */
    @TableField("text")
    private String text;

    /**
     * 链接
     */
    @TableField("link")
    private String link;

    /**
     * 开始时间
     */
    @TableField("startTime")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField("endTime")
    private Date endTime;

    @TableField("createTime")
    private Date createTime;

    @TableField("updateTime")
    private Date updateTime;

    @TableField("status")
    private Integer status;

    /**
     * 优先级
     */
    @TableField("priority")
    private Integer priority;

    @TableField("img")
    private String img;


}
