package com.lagou.course.remote;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.lagou.course.api.LessonRemoteService;
import com.lagou.course.api.dto.LessonDTO;
import com.lagou.course.entity.Course;
import com.lagou.course.entity.CourseLesson;
import com.lagou.course.mapper.CourseLessonMapper;
import com.lagou.course.service.ICourseMediaService;
import com.lagou.course.service.ILessonService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName CourseLessonController
 * @Description CourseLessonController
 * @Author zhouqian
 * @Date 2022/6/8 11:04
 * @Version 1.0
 */
@RestController
@RequestMapping("/course/lesson")
public class CourseLessonController implements LessonRemoteService {
    @Autowired
    private ILessonService lessonService;

    @Override
    @PostMapping(value = "/saveOrUpdate", consumes = "application/json")
    public boolean saveOrUpdate(@RequestBody LessonDTO lessonDTO) {
        return lessonService.saveOrUpdate(lessonDTO);
    }

    @Override
    @GetMapping(value = "/getById")
    public LessonDTO getById(@RequestParam("lessonId") Integer lessonId) {
        return lessonService.getById(lessonId);
    }

    @Override
    @GetMapping(value = "/getByIds")
    public Map<Integer, String> getByIds(@RequestParam("lessonIds") List<Integer> lessonIds) {
        return lessonService.getByIds(lessonIds);
    }

    @Override
    @GetMapping("/getReleaseCourse")
    public Integer getReleaseCourse(Integer courseId){
        return lessonService.getReleaseCourse(courseId);
    }

    @Override
    @GetMapping("/getBySectionId")
    public List<LessonDTO> getBySectionId(Integer sectionId){
        return lessonService.getBySectionId(sectionId);
    }
}
