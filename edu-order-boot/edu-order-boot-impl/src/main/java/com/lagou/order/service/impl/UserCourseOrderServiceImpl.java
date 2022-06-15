package com.lagou.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.common.exception.ServiceException;
import com.lagou.common.exception.SystemErrorType;
import com.lagou.common.response.ResponseDTO;
import com.lagou.common.util.ConvertUtil;
import com.lagou.course.api.ActivityCourseRemoteService;
import com.lagou.course.api.CourseRemoteService;
import com.lagou.course.api.dto.ActivityCourseDTO;
import com.lagou.order.api.dto.CreateShopGoodsOrderReqDTO;
import com.lagou.order.api.dto.UserCourseOrderDTO;
import com.lagou.order.api.dto.UserCourseOrderResDTO;
import com.lagou.order.api.enums.UserCourseOrderSourceType;
import com.lagou.order.entity.UserCourseOrder;
import com.lagou.order.mapper.UserCourseOrderMapper;
import com.lagou.order.service.UserCourseOrderService;
import javafx.beans.binding.ObjectExpression;
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
    @Autowired
    private ActivityCourseRemoteService activityCourseRemoteService;

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

    @Override
    public UserCourseOrderResDTO saveOrder(CreateShopGoodsOrderReqDTO reqDTO) {
        if (Objects.isNull(reqDTO.getGoodsId())){
            return null;
        }
        if (Objects.isNull(reqDTO.getUserId())){
            return null;
        }
        if (Objects.isNull(reqDTO.getSourceType())){
            return null;
        }
        // 先根据商品id查询是不是为对应的活动课程
        ResponseDTO<ActivityCourseDTO> activityCourseDTOResponseDTO = activityCourseRemoteService.getByCourseId(reqDTO.getGoodsId());
        ActivityCourseDTO activityCourseDTO = activityCourseDTOResponseDTO.getContent();
        UserCourseOrder userCourseOrder = new UserCourseOrder();
        // 生成订单号  -- 对于订单来说需要使用的是分库分表的方式使用
        userCourseOrder.setCourseId(reqDTO.getGoodsId());
        userCourseOrder.setUserId(reqDTO.getUserId());
        //userCourseOrder.setSourceType(UserCourseOrderSourceType.parse());


        return null;
    }

    @Override
    public UserCourseOrderDTO getCourseOrderByOrderNo(String orderNo) {
        LambdaQueryWrapper<UserCourseOrder> courseOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseOrderLambdaQueryWrapper.eq(UserCourseOrder::getOrderNo, orderNo);
        UserCourseOrder userCourseOrder = userCourseOrderMapper.selectOne(courseOrderLambdaQueryWrapper);
        return ConvertUtil.convert(userCourseOrder, UserCourseOrderDTO.class);
    }

    @Override
    public Boolean updateOrderStatus(String orderNo, Integer status) {
        LambdaQueryWrapper<UserCourseOrder> courseOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseOrderLambdaQueryWrapper.eq(UserCourseOrder::getOrderNo, orderNo);
        UserCourseOrder userCourseOrder = userCourseOrderMapper.selectOne(courseOrderLambdaQueryWrapper);
        LambdaUpdateWrapper<UserCourseOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserCourseOrder::getOrderNo, orderNo);
        int update = userCourseOrderMapper.update(userCourseOrder, updateWrapper);
        return update > 0;
    }

    @Override
    public List<UserCourseOrderDTO> getUserCourseOrderByUserId(Integer userId) {
        LambdaQueryWrapper<UserCourseOrder> courseOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseOrderLambdaQueryWrapper.eq(UserCourseOrder::getUserId, userId);
        List<UserCourseOrder> userCourseOrders = userCourseOrderMapper.selectList(courseOrderLambdaQueryWrapper);
        return ConvertUtil.convertList(userCourseOrders, UserCourseOrderDTO.class);
    }

    @Override
    public Integer countUserCourseOrderByCourseId(Integer courseId) {
        LambdaQueryWrapper<UserCourseOrder> courseOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseOrderLambdaQueryWrapper.eq(UserCourseOrder::getCourseId, courseId);
        return userCourseOrderMapper.selectCount(courseOrderLambdaQueryWrapper);
    }

    @Override
    public List<UserCourseOrderDTO> getOrderListByCourseId(Integer courseId) {
        LambdaQueryWrapper<UserCourseOrder> courseOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseOrderLambdaQueryWrapper.eq(UserCourseOrder::getCourseId, courseId);
        List<UserCourseOrder> userCourseOrders = userCourseOrderMapper.selectList(courseOrderLambdaQueryWrapper);
        return ConvertUtil.convertList(userCourseOrders, UserCourseOrderDTO.class);
    }
}
