package com.lagou.user.api.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName UserQueryParam
 * @Description TODO
 * @Author zhouqian
 * @Date 2022/4/5 09:31
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryParam {
    private Integer pageNum;
    private Integer pageSize;
    private String phone;
    private Integer userId;
    private Date startCreateTime;
    private Date endCreateTime;
}
