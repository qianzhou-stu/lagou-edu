package com.lagou.course.api;

import com.lagou.common.response.ResponseDTO;
import com.lagou.course.api.dto.ActivityCourseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName ActivityCourseRemoteService
 * @Description 活动Feign远程调用
 * @Author zhouqian
 * @Date 2022/6/6 18:31
 * @Version 1.0
 */
//@FeignClient(name = "${remote.feign.edu-course-boot.name:edu-course-boot}", path = "/activityCourse")
@FeignClient(name = "edu-course-boot.ActivityCourseRemoteService", path = "/activityCourse")
public interface ActivityCourseRemoteService {

    /**
     * 保存课程活动
     * @param reqDTO
     * @return ResponseDTO<?>
     */
    @PostMapping("/saveActivityCourse")
    ResponseDTO<?> saveActivityCourse(@RequestBody ActivityCourseDTO reqDTO);

    /**
     * 更新课程活动状态
     * @param reqDTO
     * @return ResponseDTO<?>
     */
    @PostMapping("updateActivityCourseStatus")
    ResponseDTO<?> updateActivityCourseStatus(@RequestBody ActivityCourseDTO reqDTO);

    /**
     * 根据id获取课程活动
     * @param id
     * @return ResponseDTO<ActivityCourseDTO>
     */
    @GetMapping("/getById")
    ResponseDTO<ActivityCourseDTO> getById(@RequestParam("id") Integer id);

    /**
     * 根据课程id获取课程活动
     * @param courseId
     * @return ResponseDTO<ActivityCourseDTO>
     */
    @GetMapping("/getByCourseId")
    ResponseDTO<ActivityCourseDTO>getByCourseId(@RequestParam("courseId") Integer courseId);

    /**
     * 根绝订单号和课程id更新课程活动库存
     * @param courseId
     * @param orderNo
     * @return ResponseDTO<?>
     */
    @PostMapping("/updateActivityCourseStock")
    ResponseDTO<?> updateActivityCourseStock(@RequestParam("courseId")Integer courseId,@RequestParam("orderNo")String orderNo);
}
