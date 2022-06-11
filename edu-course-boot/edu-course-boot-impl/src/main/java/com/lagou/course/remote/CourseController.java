package com.lagou.course.remote;

import com.lagou.course.api.CourseRemoteService;
import com.lagou.course.api.dto.CourseDTO;
import com.lagou.course.api.dto.PageResultDTO;
import com.lagou.course.api.param.CourseQueryParam;
import com.lagou.course.service.ICourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName CourseService
 * @Description CourseController远程调用接口impl
 * @Author zhouqian
 * @Date 2022/6/7 20:21
 * @Version 1.0
 */
@RestController
@RequestMapping("/course")
@Slf4j
public class  CourseController implements CourseRemoteService {

    @Autowired
    private ICourseService courseService;

    @Override
    @GetMapping("/getAllCourses")
    public List<CourseDTO> getAllCourses(@RequestParam(required = false,name = "userId") Integer userId) {
        return courseService.getAllCourses(userId);
    }

    @Override
    @GetMapping("/getPurchasedCourse")
    public List<CourseDTO> getPurchasedCourse(@RequestParam("userId") Integer userId) {
        return courseService.getPurchasedCourse(userId);
    }

    @Override
    public CourseDTO getCourseById(Integer courseId, Integer userId) {
        return null;
    }

    @Override
    public boolean saveOrUpdateCourse(CourseDTO courseDTO) {
        return false;
    }

    @Override
    public PageResultDTO<CourseDTO> getQueryCourses(CourseQueryParam courseQueryParam) {
        return null;
    }
}
