package com.lagou.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dangdang.ddframe.rdb.sharding.keygen.KeyGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.common.constant.CacheDefine;
import com.lagou.common.exception.ServiceException;
import com.lagou.common.exception.SystemErrorType;
import com.lagou.common.response.ResponseDTO;
import com.lagou.common.util.ConvertUtil;
import com.lagou.common.util.ValidateUtils;
import com.lagou.course.api.ActivityCourseRemoteService;
import com.lagou.course.api.CourseRemoteService;
import com.lagou.course.api.dto.ActivityCourseDTO;
import com.lagou.course.api.dto.CourseDTO;
import com.lagou.course.api.enums.CourseStatus;
import com.lagou.order.api.dto.CreateShopGoodsOrderReqDTO;
import com.lagou.order.api.dto.UserCourseOrderDTO;
import com.lagou.order.api.dto.UserCourseOrderResDTO;
import com.lagou.order.api.enums.UserCourseOrderSourceType;
import com.lagou.order.api.enums.UserCourseOrderStatus;
import com.lagou.order.entity.UserCourseOrder;
import com.lagou.order.mapper.UserCourseOrderMapper;
import com.lagou.order.service.UserCourseOrderService;
import javafx.beans.binding.ObjectExpression;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
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
@Slf4j
public class UserCourseOrderServiceImpl extends ServiceImpl<UserCourseOrderMapper, UserCourseOrder> implements UserCourseOrderService {
    @Autowired
    private UserCourseOrderMapper userCourseOrderMapper;
    @Autowired
    private ActivityCourseRemoteService activityCourseRemoteService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private KeyGenerator keyGenerator;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


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


    /**
     * 创建订单
     * @param reqDTO
     * @return
     */
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
        // 校验商品信息
        CourseDTO courseDTO = courseRemoteService.getCourseById(reqDTO.getGoodsId(), reqDTO.getUserId());
        log.info("saveOrder - courseRemoteService.getCourseById - goodsId:{} courseDTO:{}",reqDTO.getGoodsId(), JSON.toJSONString(courseDTO));
        ValidateUtils.isFalse(null == courseDTO, "课程信息为空");
        ValidateUtils.isTrue(courseDTO.getStatus().equals(CourseStatus.PUTAWAY.getCode()), "课程状态错误");
        UserCourseOrder userCourseOrder = checkSuccessBuyGoods(reqDTO.getGoodsId(), reqDTO.getUserId());
        ValidateUtils.isTrue(null == userCourseOrder, "已成功购买过该课程");
        userCourseOrder = checkCreateBuyGoods(reqDTO.getGoodsId(), reqDTO.getUserId());
        if(null != userCourseOrder) {
            //已经下单还有效
            return new UserCourseOrderResDTO(userCourseOrder.getOrderNo());
        }
        // 创建商品订单
        UserCourseOrder saveOrder = buildUserCourseOrder(reqDTO.getGoodsId(), reqDTO.getUserId(), reqDTO.getSourceType());
        String activityCourseStr = redisTemplate.opsForValue().get(CacheDefine.ActivityCourse.getKey(reqDTO.getGoodsId()));
        ActivityCourseDTO activityCourseCache = null;
        if (StringUtils.isNotBlank(activityCourseStr)){
            activityCourseCache = JSON.parseObject(activityCourseStr, ActivityCourseDTO.class);
            long cacheRes = redisTemplate.opsForValue().increment(CacheDefine.ActivityCourse.getStockKey(activityCourseCache.getCourseId()))
        }
        redisTemplate.opsForValue().increment();


        // 先根据商品id查询是不是为对应的活动课程
        ResponseDTO<ActivityCourseDTO> activityCourseDTOResponseDTO = activityCourseRemoteService.getByCourseId(reqDTO.getGoodsId());
        ActivityCourseDTO activityCourseDTO = activityCourseDTOResponseDTO.getContent();
        // UserCourseOrder userCourseOrder = new UserCourseOrder();
        // 生成订单号  -- 对于订单来说需要使用的是分库分表的方式使用
        userCourseOrder.setCourseId(reqDTO.getGoodsId());
        userCourseOrder.setUserId(reqDTO.getUserId());
        //userCourseOrder.setSourceType(UserCourseOrderSourceType.parse());


        return null;
    }

    /**
     * 查询用户成功状态订单
     * @param goodId
     * @param userId
     * @return
     */
    UserCourseOrder checkSuccessBuyGoods(Integer goodId, Integer userId){
        ValidateUtils.notNullParam(userId);
        ValidateUtils.isTrue(userId > 0, "用户id错误");
        ValidateUtils.notNullParam(goodId);
        ValidateUtils.isTrue(goodId > 0, "课程id错误");
        return getOne(new QueryWrapper<UserCourseOrder>().eq("course_id", goodId).eq("user_id", userId).eq("status", UserCourseOrderStatus.SUCCESS.getCode()));
    }

    /**
     * 查询用户成功创建的订单状态
     * @param goodId
     * @param userId
     * @return
     */
    UserCourseOrder checkCreateBuyGoods(Integer goodId, Integer userId){
        ValidateUtils.notNullParam(userId);
        ValidateUtils.isTrue(userId > 0, "用户id错误");
        ValidateUtils.notNullParam(goodId);
        ValidateUtils.isTrue(goodId > 0, "课程id错误");
        return getOne(new QueryWrapper<UserCourseOrder>().eq("course_id", goodId).eq("user_id", userId).eq("status", UserCourseOrderStatus.CREATE.getCode()));
    }

    /**
     * 构建商品订单信息
     * @param goodId
     * @param userId
     * @param sourceType
     * @return
     */
    UserCourseOrder buildUserCourseOrder(Integer goodId, Integer userId, UserCourseOrderSourceType sourceType){
        UserCourseOrder saveUserCourseOrder = new UserCourseOrder();
        // 使用sharding-jdbc的自动生成id的策略，自动生成对应的分库分表的id值
        saveUserCourseOrder.setId(Long.parseLong(keyGenerator.generateKey().toString()));
        saveUserCourseOrder.setCourseId(goodId);
        saveUserCourseOrder.setCreateTime(new Date());
        saveUserCourseOrder.setOrderNo(keyGenerator.generateKey().toString());
        saveUserCourseOrder.setSourceType(sourceType.getCode());
        saveUserCourseOrder.setUpdateTime(saveUserCourseOrder.getCreateTime());
        saveUserCourseOrder.setUserId(userId);
        return saveUserCourseOrder;
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
