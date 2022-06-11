package com.lagou.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.common.exception.ServiceException;
import com.lagou.common.exception.SystemErrorType;
import com.lagou.order.entity.UserCourseOrder;
import com.lagou.order.mapper.UserCourseOrderMapper;
import com.lagou.order.service.UserCourseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName UserCourseOrderServiceImpl
 * @Description UserCourseOrderServiceImpl
 * @Author zhouqian
 * @Date 2022/6/10 20:24
 * @Version 1.0
 */
@Service
public class UserCourseOrderServiceImpl extends ServiceImpl<UserCourseOrderMapper, UserCourseOrder> implements UserCourseOrderService {
    @Autowired
    private UserCourseOrderMapper userCourseOrderMapper;

    @Override
    public Integer countUserCourseOrderByCourseIds(Integer userId, List<Integer> courseIds) {
        if (Objects.isNull(userId)){
            throw new ServiceException(SystemErrorType.NOT_UNIQUE_USER_PRIMARY_KEY, "用户的id不能为空");
        }
        LambdaQueryWrapper<UserCourseOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCourseOrder::getUserId, userId);
        queryWrapper.in(UserCourseOrder::getCourseId, courseIds);
        return userCourseOrderMapper.selectCount(queryWrapper);
    }
}
