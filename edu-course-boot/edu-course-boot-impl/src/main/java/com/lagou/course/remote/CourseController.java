package com.lagou.course.remote;

import com.lagou.common.entity.vo.Result;
import com.lagou.common.response.EduEnum;
import com.lagou.course.api.CourseRemoteService;
import com.lagou.course.api.dto.CourseDTO;
import com.lagou.course.api.dto.PageResultDTO;
import com.lagou.course.api.param.CourseQueryParam;
import com.lagou.course.service.ICourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    /**
     * 查询选课信息
     * @param userId
     * @return List<CourseDTO>
     */
    @Override
    @GetMapping("/getAllCourses")
    public List<CourseDTO> getAllCourses(@RequestParam(required = false,name = "userId") Integer userId) {
        return courseService.getAllCourses(userId);
    }

    /**
     * 根据用户id查询购买课程
     * @param userId
     * @return List<CourseDTO>
     */
    @Override
    @GetMapping("/getPurchasedCourse")
    public List<CourseDTO> getPurchasedCourse(@RequestParam("userId") Integer userId) {
        return courseService.getPurchasedCourse(userId);
    }

    /**
     * 根据用户id和课程id查询课信息
     * @param courseId
     * @param userId
     * @return CourseDTO
     */
    @Override
    @GetMapping("/getCourseById")
    public CourseDTO getCourseById(Integer courseId, Integer userId) {
        // 根据用户id和课程id获取对应的课程信息
        return courseService.getCourseById(courseId, userId);
    }

    /**
     * 保存课程信息
     * @param courseDTO
     * @return boolean
     */
    @Override
    @GetMapping("/saveOrUpdateCourse")
    public boolean saveOrUpdateCourse(CourseDTO courseDTO) {
        return courseService.saveOrUpdateCourse(courseDTO);
    }

    /**
     * 根据查询条件分页查询课程信息
     * @param courseQueryParam
     * @return
     */
    @Override
    @GetMapping("/getQueryCourses")
    public PageResultDTO<CourseDTO> getQueryCourses(CourseQueryParam courseQueryParam) {
        return courseService.getQueryCourses(courseQueryParam);
    }

    @Override
    @PostMapping(value = "/changeState")
    public Result changeState(@RequestParam("courseId") Integer courseId,
                              @RequestParam("status") Integer status) {
        Boolean b = courseService.changeState(courseId, status);
        if (b){
            return Result.success();
        }
        return Result.fail(EduEnum.UPDATE_FAILURE);
    }
}
