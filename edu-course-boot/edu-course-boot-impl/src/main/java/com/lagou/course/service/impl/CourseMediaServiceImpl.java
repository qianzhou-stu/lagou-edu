package com.lagou.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.common.response.ResponseDTO;
import com.lagou.common.util.ConvertUtil;
import com.lagou.course.api.dto.LessonDTO;
import com.lagou.course.api.dto.MediaDTO;
import com.lagou.course.entity.CourseLesson;
import com.lagou.course.entity.CourseMedia;
import com.lagou.course.mapper.CourseLessonMapper;
import com.lagou.course.mapper.CourseMediaMapper;
import com.lagou.course.service.ICourseMediaService;
import com.lagou.course.service.ILessonService;
import com.lagou.order.api.UserCourseOrderRemoteService;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName LessonServiceImpl
 * @Description CourseMediaServiceImpl实现类
 * @Author zhouqian
 * @Date 2022/6/8 09:09
 * @Version 1.0
 */
@Service
@Slf4j
public class CourseMediaServiceImpl extends ServiceImpl<CourseMediaMapper, CourseMedia> implements ICourseMediaService {
    @Autowired
    private CourseMediaMapper courseMediaMapper;
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private UserCourseOrderRemoteService userCourseOrderRemoteService;

    @Override
    public MediaDTO getByLessonId(Integer lessonId) {
        LambdaQueryWrapper<CourseMedia> courseMediaLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseMediaLambdaQueryWrapper.eq(CourseMedia::getLessonId, lessonId);
        CourseMedia courseMedia = courseMediaMapper.selectOne(courseMediaLambdaQueryWrapper);
        MediaDTO mediaDTO = new MediaDTO();
        BeanUtils.copyProperties(courseMedia, mediaDTO);
        return mediaDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrSaveMedia(MediaDTO mediaDTO) {
        CourseMedia courseMedia = ConvertUtil.convert(mediaDTO, CourseMedia.class);
        Integer lessonId = mediaDTO.getLessonId();
        LessonDTO lessonDTO = lessonService.getById(lessonId);
        if(lessonDTO == null){
            log.error("没有对应的lesson信息 mediaDTO:{}", JSON.toJSONString(mediaDTO));
            return;
        }
        // 保存课程lesson信息  最主要保存的是时间长度信息
        lessonDTO.setDuration(mediaDTO.getDuration());
        lessonService.saveOrUpdate(lessonDTO);
        Integer courseId = lessonDTO.getCourseId();
        Integer sectionId = lessonDTO.getSectionId();
        courseMedia.setCourseId(courseId);
        courseMedia.setSectionId(sectionId);
        QueryWrapper<CourseMedia> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("lesson_id", lessonId);
        queryWrapper.eq("section_id", sectionId);
        queryWrapper.eq("is_del", Boolean.FALSE);
        List<CourseMedia> courseMediaList = courseMediaMapper.selectList(queryWrapper);
        LocalDateTime localDateTime = LocalDateTime.now();
        if (!CollectionUtils.isEmpty(courseMediaList)){
            // 更新
            Integer id = courseMediaList.get(0).getId();
            courseMedia.setId(id);
            courseMedia.setUpdateTime(localDateTime);
            courseMedia.setCreateTime(localDateTime);
            courseMediaMapper.updateById(courseMedia);
        }else{
            // 创建
            courseMedia.setUpdateTime(localDateTime);
            courseMedia.setCreateTime(localDateTime);
            courseMediaMapper.insert(courseMedia);
        }
    }

    @Override
    public byte[] getCourseMediaDKByFileId(String fileId, String edk, Integer userId) {
        QueryWrapper<CourseMedia> query = new QueryWrapper<CourseMedia>().eq("file_id", fileId).eq("is_del", Boolean.FALSE);
        List<CourseMedia> courseMediaList = courseMediaMapper.selectList(query);
        if (CollectionUtils.isEmpty(courseMediaList)){
            log.info("fileId:{}对应的媒体资源不存在", fileId);
            return null;
        }
        // 拿到对应的课程列表id
        List<Integer> courseIdList = new ArrayList<>(courseMediaList.size());
        // 拿到对应的课时列表id
        List<Integer> lessonIdList = new ArrayList<>(courseMediaList.size());

        courseMediaList.forEach((item)->{
            courseIdList.add(item.getCourseId());
            lessonIdList.add(item.getLessonId());
        });

        List<CourseLesson> courseLessons = lessonService.listByIds(lessonIdList);
        if (CollectionUtils.isEmpty(courseLessons)){
            log.info("lessonIdList:{}所对应的的课时信息不存在", lessonIdList);
            return null;
        }

        CourseMedia courseMedia = courseMediaList.get(0);
        String fileEdk = courseMedia.getFileEdk();
        // 如果传入的edk不一致，那就返回为null
        if (!Objects.equals(fileEdk, edk)){
            log.info("媒体资源的EDK不匹配，fileId:{}, fromEDK:{}", fileId, fileEdk);
            return null;
        }

        String fileDk = courseMedia.getFileDk();
        if (StringUtils.isBlank(fileDk)){
            log.info("fileId:{}对应的媒体资源DK不存在",fileId);
            return null;
        }

        byte[] dkBytes = Base64.getDecoder().decode(fileDk);

        // 遍历课时列表， 检查是否存在免费课时
        for (CourseLesson courseLesson : courseLessons) {
            // 存在一个免费课时，直接返回
            if (courseLesson.getIsFree() != null && courseLesson.getIsFree()){
                log.info("lessonId:{}是免费课时,fileId:{},mediaDk:{},dkBytes:{} ", courseLesson.getId(), fileId, fileDk, dkBytes);
                return dkBytes;
            }
        }
        // 当用户ID为null，又不是免费课时，直接返回null
        if(userId == null){
            log.info("用户为空 并且课时不为 免费课程");
            return null;
        }
        ResponseDTO<Integer> responseDTO = userCourseOrderRemoteService.countUserCourseOrderByCourseIds(userId, courseIdList);
        if (responseDTO.isSuccess()){
            log.info("调用用户订单失败 responseDTO：{}", JSON.toJSONString(responseDTO));
            return null;
        }
        Integer orderCount = responseDTO.getContent();
        if (orderCount <= 0) {
            log.info("用户未购买该课程,courseIdList:{}, fileId:{}，userId:{}", courseIdList, fileId, userId);
            return null;
        }
        return dkBytes;
    }
}
