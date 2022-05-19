package com.lagou.ad.api.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


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
public class PromotionSpaceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 广告位key
     */
    @TableField("spaceKey")
    private String spaceKey;

    @TableField("createTime")
    private Date createTime;

    @TableField("updateTime")
    private Date updateTime;

    @TableField("isDel")
    private Integer isDel;

    private List<PromotionAdDTO> promotionAdDTOS;
}
