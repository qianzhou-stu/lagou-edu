package com.lagou.boss.service.impl;

import com.lagou.boss.service.ILessonService;
import com.lagou.common.constant.MQConstant;
import com.lagou.common.mq.RocketMqService;
import com.lagou.common.mq.dto.BaseMqDTO;
import com.lagou.common.result.ResultCode;
import com.lagou.common.util.ValidateUtils;
import com.lagou.course.api.LessonRemoteService;
import com.lagou.course.api.dto.LessonDTO;
import com.lagou.course.api.enums.CourseLessonStatus;
import com.lagou.message.api.dto.LessonStatusReleaseDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class LessonServiceImpl implements ILessonService {

    @Autowired
    private LessonRemoteService lessonRemoteService;
    @Autowired
    private RocketMqService rocketMqService;
    @Override
    public boolean saveOrUpdate(LessonDTO lessonDTO) {
        boolean isRelease = false;
        if (lessonDTO.getId() != null && lessonDTO.getStatus() != null && lessonDTO.getStatus().equals(CourseLessonStatus.RELEASE.getCode())){
            // 查看是否是上架操作
            LessonDTO lessonDB = lessonRemoteService.getById(lessonDTO.getId());
            ValidateUtils.notNull(lessonDB, ResultCode.ALERT_ERROR.getState(), "课程信息查询为空");
            isRelease = !lessonDB.getStatus().equals(CourseLessonStatus.RELEASE.getCode()); // 准备发布课程
        }
        boolean res = lessonRemoteService.saveOrUpdate(lessonDTO);
        if (res && isRelease){
            rocketMqService.convertAndSend(MQConstant.Topic.LESSON_STATUS_RELEASE, new BaseMqDTO<LessonStatusReleaseDTO>(new LessonStatusReleaseDTO(lessonDTO.getId()), UUID.randomUUID().toString()));
        }
        return res;
    }
}
