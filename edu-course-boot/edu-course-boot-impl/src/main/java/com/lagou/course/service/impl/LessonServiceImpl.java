package com.lagou.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.course.api.dto.LessonDTO;
import com.lagou.course.api.enums.CourseLessonStatus;
import com.lagou.course.entity.CourseLesson;
import com.lagou.course.mapper.CourseLessonMapper;
import com.lagou.course.mapper.CourseMapper;
import com.lagou.course.service.ILessonService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.ls.LSInput;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName LessonServiceImpl
 * @Description LessonServiceImpl实现类
 * @Author zhouqian
 * @Date 2022/6/8 09:09
 * @Version 1.0
 */
@Service
public class LessonServiceImpl extends ServiceImpl<CourseLessonMapper, CourseLesson> implements ILessonService {
    @Autowired
    private CourseLessonMapper courseLessonMapper;

    @Override
    public boolean saveOrUpdate(LessonDTO lessonDTO) {
        CourseLesson lesson = new CourseLesson();
        BeanUtils.copyProperties(lessonDTO, lesson);
        Date date = new Date();
        if (lesson.getId() == null){
            lesson.setCreateTime(date);
            lesson.setUpdateTime(date);
            int num = courseLessonMapper.insert(lesson);
            if (num > 0) return true;
            return false;
        }else {
            lesson.setUpdateTime(date);
            int num = courseLessonMapper.updateById(lesson);
            if (num > 0) return true;
            return false;
        }
    }

    @Override
    public LessonDTO getById(Integer lessonId) {
        CourseLesson courseLesson = courseLessonMapper.selectById(lessonId);
        if (courseLesson == null){
            return null;
        }
        LessonDTO lessonDTO = new LessonDTO();
        BeanUtils.copyProperties(courseLesson, lessonDTO);
        return lessonDTO;
    }

    @Override
    public Map<Integer, String> getByIds(List<Integer> lessonIds) {
        QueryWrapper<CourseLesson> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", lessonIds);
        List<CourseLesson> courseLessons = courseLessonMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(courseLessons)){
            return Collections.emptyMap();
        }
        return courseLessons.stream().collect(Collectors.toMap(CourseLesson::getCourseId, CourseLesson::getTheme));
    }

    @Override
    public Integer getReleaseCourse(Integer courseId) {
        QueryWrapper<CourseLesson> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("status", CourseLessonStatus.RELEASE.getCode());
        queryWrapper.eq("is_del", Boolean.FALSE);
        return courseLessonMapper.selectCount(queryWrapper);
    }

    @Override
    public List<LessonDTO> getBySectionId(Integer sectionId) {
        QueryWrapper<CourseLesson> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("section_id", sectionId);
        queryWrapper.eq("is_del", Boolean.FALSE);
        queryWrapper.orderByDesc("order_num");
        List<CourseLesson> courseLessons = courseLessonMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(courseLessons)){
            return Collections.emptyList();
        }
        List<LessonDTO> lessonDTOS = new ArrayList<>();
        for (CourseLesson courseLesson : courseLessons) {
            LessonDTO lessonDTO = new LessonDTO();
            BeanUtils.copyProperties(courseLesson, lessonDTO);
            lessonDTOS.add(lessonDTO);
        }
        return lessonDTOS;
    }
}
