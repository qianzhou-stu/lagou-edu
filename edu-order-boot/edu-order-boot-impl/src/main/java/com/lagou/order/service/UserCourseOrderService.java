package com.lagou.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.order.entity.UserCourseOrder;

import java.util.List;


/**
 * @ClassName UserCourseOrderService
 * @Description TODO
 * @Author zhouqian
 * @Date 2022/6/10 20:24
 * @Version 1.0
 */

public interface UserCourseOrderService extends IService<UserCourseOrder> {
    Integer countUserCourseOrderByCourseIds(Integer userId, List<Integer> courseIds);
}
