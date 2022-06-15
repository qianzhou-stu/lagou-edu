package com.lagou.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.common.constant.CacheDefine;
import com.lagou.common.date.DateUtil;
import com.lagou.common.response.ResponseDTO;
import com.lagou.course.api.dto.ActivityCourseDTO;
import com.lagou.course.api.enums.ActivityCourseStatus;
import com.lagou.course.entity.ActivityCourse;
import com.lagou.course.mapper.ActivityCourseMapper;
import com.lagou.course.service.IActivityCourseService;
import com.lagou.order.api.UserCourseOrderRemoteService;
import com.lagou.order.api.dto.UserCourseOrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;

import javax.management.MBeanAttributeInfo;
import javax.swing.*;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ActivityCourseServiceImpl
 * @Description ActivityCourseServiceImpl
 * @Author zhouqian
 * @Date 2022/6/8 10:49
 * @Version 1.0
 */
@Service
@Slf4j
public class ActivityCourseServiceImpl extends ServiceImpl<ActivityCourseMapper, ActivityCourse> implements IActivityCourseService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ActivityCourseMapper activityCourseMapper;

    @Autowired
    private UserCourseOrderRemoteService userCourseOrderRemoteService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean saveActivityCourse(ActivityCourseDTO reqDTO) {
        log.info("saveActivityCourse - reqDTO:{}", JSON.toJSONString(reqDTO));
        if (!checkParam(reqDTO)){
            return false;
        }
        ActivityCourse activityCourse = new ActivityCourse();
        BeanUtils.copyProperties(reqDTO, activityCourse);
        activityCourse.setCreateTime(new Date());
        activityCourse.setCreateUser("auto"); // TODO 记得取当前登陆用户
        activityCourse.setUpdateTime(activityCourse.getCreateTime());
        activityCourse.setUpdateUser("auto"); // TODO 记得取当前登陆用户
        int insert = activityCourseMapper.insert(activityCourse);
        return insert >= 1;
    }

    private Boolean checkParam(ActivityCourseDTO reqDTO) {
        if (Objects.isNull(reqDTO)){
            return false;
        }
        if (Objects.isNull(reqDTO.getAmount())){
            return false;
        }
        if (reqDTO.getAmount() < 0){
            return false;
        }
        if (reqDTO.getCourseId() < 0){
            return false;
        }
        if (Objects.isNull(reqDTO.getStock())){
            return false;
        }
        if (reqDTO.getStock() < 0){
            return false;
        }
        if (Objects.isNull(reqDTO.getBeginTime())){
            return false;
        }
        if (Objects.isNull(reqDTO.getEndTime())){
            return false;
        }
        if (!DateUtil.isBefore(reqDTO.getEndTime(), reqDTO.getBeginTime())){
            return false;
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateActivityCourseStatus(ActivityCourseDTO reqDTO) {
        log.info("updateActivityCourseStatus - reqDTO:{}", JSON.toJSON(reqDTO));
        if (Objects.isNull(reqDTO)){
            return false;
        }
        if (reqDTO.getId() == null){
            return false;
        }
        if (reqDTO.getId() <= 0){
            return false;
        }
        if (Objects.isNull(ActivityCourseStatus.parse(reqDTO.getStatus()))){
            return false;
        }
        // 根据传入的id，获取激活活动的课程信息
        ActivityCourse activityCourse = getById(reqDTO.getId());
        if (Objects.isNull(activityCourse)){
            return false;
        }
        if (reqDTO.getStatus().equals(activityCourse.getStatus())){
            return true;
        }
        activityCourse.setStatus(reqDTO.getStatus());
        boolean res = updateById(activityCourse);
        if (!res){
            return false;
        }
        ActivityCourseDTO dto = new ActivityCourseDTO();
        BeanUtils.copyProperties(activityCourse, dto);
        redisTemplate.opsForValue().set(CacheDefine.ActivityCourse.getKey(activityCourse.getCourseId()), JSON.toJSONString(dto), DateUtil.getSecond(new Date(), activityCourse.getEndTime()), TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(CacheDefine.ActivityCourse.getStockKey(activityCourse.getCourseId()), activityCourse.getStock().toString(),DateUtil.getSecond(new Date(), activityCourse.getEndTime()), TimeUnit.SECONDS);
        return true;
    }

    @Override
    public ActivityCourseDTO getByCourseId(Integer courseId) {
        log.info("getByCourseId - courseId:{}", courseId);
        if (Objects.isNull(courseId)){
            return null;
        }
        if (courseId <= 0){
            return null;
        }
        ActivityCourse activityCourse = getOne(new QueryWrapper<ActivityCourse>().eq("course_id", courseId));
        if (null == activityCourse){
            return  null;
        }
        ActivityCourseDTO activityCourseDTO = new ActivityCourseDTO();
        BeanUtils.copyProperties(activityCourse, activityCourseDTO);
        return activityCourseDTO;
    }

    @Override
    public Boolean updateActivityCourseStock(Integer courseId, String orderNo) {
        log.info("updateActivityCourseStock - courseId:{} orderNo:{}", courseId, orderNo);
        if (Objects.isNull(orderNo)){
            return false;
        }
        ResponseDTO<UserCourseOrderDTO> userCourseOrderDTOResponseDTO = userCourseOrderRemoteService.getCourseOrderByOrderNo(orderNo);
        // 对相应内容做出判断的决定
        UserCourseOrderDTO userCourseOrderDTO = userCourseOrderDTOResponseDTO.getContent();
        if (userCourseOrderDTO.getActivityCourseId() == 0){
            return false;
        }
        ActivityCourseDTO activityCourseDTO = getByCourseId(courseId);
        ActivityCourse activityCourse = new ActivityCourse();
        BeanUtils.copyProperties(activityCourseDTO, activityCourse);
        int update = activityCourseMapper.updateById(activityCourse);
        if (update < 1){
            return  false;
        }
        return true;
    }

}
