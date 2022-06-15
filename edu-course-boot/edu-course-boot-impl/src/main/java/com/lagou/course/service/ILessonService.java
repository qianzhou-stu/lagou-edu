package com.lagou.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.course.api.dto.LessonDTO;
import com.lagou.course.entity.CourseLesson;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;

public interface ILessonService extends IService<CourseLesson> {

    public boolean saveOrUpdate(LessonDTO lessonDTO);

    public LessonDTO getById(Integer lessonId);

    public Map<Integer,String> getByIds(List<Integer> lessonIds);

    public Integer getReleaseCourse(Integer courseId);

    public List<LessonDTO> getBySectionId(Integer sectionId);
}
