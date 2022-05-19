package com.lagou.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhouqian
 * @since 2022-04-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_phone_verification_code")
public class PhoneVerificationCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码
     */
    private String verificationCode;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 验证码是否校验过
     */
    @TableField("isCheck")
    private Boolean ischeck;

    /**
     * 校验次数
     */
    private Integer checkTimes;


}
