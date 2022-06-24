package com.lagou.bom.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.lagou.bom.course.model.response.CourseLessonRespVo;
import com.lagou.bom.course.model.response.CourseMediaRespVo;
import com.lagou.bom.course.model.response.CourseSectionListResult;
import com.lagou.bom.course.model.response.CourseSectionRespVo;
import com.lagou.bom.course.service.SectionService;
import com.lagou.common.response.ResponseDTO;
import com.lagou.course.api.CoursePlayHistoryRemoteService;
import com.lagou.course.api.CourseRemoteService;
import com.lagou.course.api.SectionRemoteService;
import com.lagou.course.api.dto.CourseDTO;
import com.lagou.course.api.dto.LessonDTO;
import com.lagou.course.api.dto.MediaDTO;
import com.lagou.course.api.dto.SectionDTO;
import com.lagou.order.api.UserCourseOrderRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
@Service
public class SectionServiceImpl implements SectionService {
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private UserCourseOrderRemoteService userCourseOrderRemoteService;
    @Autowired
    private CoursePlayHistoryRemoteService coursePlayHistoryRemoteService;
    @Autowired
    private SectionRemoteService sectionRemoteService;
    @Override
    public CourseSectionListResult getSectionInfoByCourseId(Integer userId, Integer courseId) {
        CourseSectionListResult result = new CourseSectionListResult();
        if (Objects.isNull(courseId)){
            return result;
        }
        CourseDTO course = courseRemoteService.getCourseById(courseId, userId);
        if(course == null){
            return result;
        }
        // 设置课程名称
        result.setCourseName(course.getCourseName());
        // 设置课程封面图
        result.setCoverImage(course.getCourseListImg());
        // 保存用户已经学习课时ID
        Set hasLearnedLessonIds = new HashSet();
        if (userId != null){
            result.setHasBuy(checkHasBuy(userId, courseId));
            // 获取某一课已经学习课时
            List lessonIds = coursePlayHistoryRemoteService.hasStudyLessons(userId,courseId);
            if(!CollectionUtils.isEmpty(lessonIds)){
                hasLearnedLessonIds.addAll(lessonIds);
            }
        }
        List<SectionDTO> sectionDTOS = sectionRemoteService.getSectionAndLesson(courseId);
        if (CollectionUtils.isEmpty(sectionDTOS)) {
            return result;
        }
        List<CourseSectionRespVo> courseSectionRespVos = new LinkedList<>();
        // 拷贝课程章节信息
        for (SectionDTO sectionDTO : sectionDTOS) {
            CourseSectionRespVo sectionRespVo = new CourseSectionRespVo();
            BeanUtils.copyProperties(sectionDTO, sectionRespVo);
            List<LessonDTO> lessonDTOS = sectionDTO.getLessonDTOS();
            List<CourseLessonRespVo> courseLessonRespVos = copyCourseLessonRespVos(lessonDTOS,result.isHasBuy(),hasLearnedLessonIds);
            //章节下没有课时信息时，不进行展示
            if (courseLessonRespVos == null){
                continue;
            }
            sectionRespVo.setCourseLessons(courseLessonRespVos);
            courseSectionRespVos.add(sectionRespVo);
        }
        result.setCourseSectionList(courseSectionRespVos);
        return result;
    }

    private List<CourseLessonRespVo> copyCourseLessonRespVos(List<LessonDTO> lessonDTOS, boolean hasBuy, Set hasLearnedLessonIds){
        if (CollectionUtils.isEmpty(lessonDTOS)) {
            return null;
        }
        List<CourseLessonRespVo> courseLessonRespVos = new LinkedList<>();
        for (LessonDTO lessonDTO : lessonDTOS) {
            CourseLessonRespVo lessonRespVo = copySingleCourseLessonRespVo(lessonDTO);
            // 购买的课程，设置课时设置为可见
            if(hasBuy){
                lessonRespVo.setCanPlay(hasBuy);
            }
            courseLessonRespVos.add(lessonRespVo);
            lessonRespVo.setHasLearned(hasLearnedLessonIds.contains(lessonDTO.getId()));
        }
        return courseLessonRespVos;
    }

    private CourseLessonRespVo copySingleCourseLessonRespVo(LessonDTO lessonDTO){
        CourseLessonRespVo lessonRespVo = new CourseLessonRespVo();
        BeanUtils.copyProperties(lessonDTO, lessonRespVo);
        if (lessonDTO.getIsFree()){
            lessonRespVo.setCanPlay(Boolean.TRUE);
        }
        // 复制视频信息
        MediaDTO videoMediaDTO = lessonDTO.getMediaDTO();
        if (videoMediaDTO != null){
            CourseMediaRespVo videoMediaRespVo = new CourseMediaRespVo();
            BeanUtils.copyProperties(videoMediaDTO, videoMediaRespVo);
            supplementDurationOfVideo(videoMediaRespVo);
            lessonRespVo.setVideoMediaDTO(videoMediaRespVo);
            lessonRespVo.setHasVideo(true);
        }
        return lessonRespVo;
    }

    private void supplementDurationOfVideo(CourseMediaRespVo courseMediaRespVo){
        String duration = courseMediaRespVo.getDuration();
        Integer durationNum = courseMediaRespVo.getDurationNum();
        if (durationNum == null && StringUtils.isNotBlank(duration)){
            durationNum = getDurationNum(duration, durationNum);
            courseMediaRespVo.setDurationNum(durationNum);
        }
    }

    private Integer getDurationNum(String duration, Integer durationNum){
        String[] times = duration.split(":");
        if (times.length == 3){
            durationNum = Integer.parseInt(times[0]) * 60 * 60 + Integer.parseInt(times[1]) * 60 + Integer.parseInt(times[2]);
        }
        if (times.length == 2){
            durationNum = Integer.parseInt(times[0]) * 60 + Integer.parseInt(times[1]);
        }
        if (times.length == 1){
            durationNum = Integer.parseInt(times[0]);
        }
        return durationNum;
    }

    private boolean checkHasBuy(Integer userId, Integer courseId) {
        ResponseDTO<Integer> responseDTO = userCourseOrderRemoteService.countUserCourseOrderByCourseIds(userId, Arrays.asList(courseId));
        log.info("判断用户是否购买 userId:{}  responseDTO:{}", userId, JSON.toJSONString(responseDTO));
        if (responseDTO.isSuccess() && responseDTO.getContent() > 0){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
