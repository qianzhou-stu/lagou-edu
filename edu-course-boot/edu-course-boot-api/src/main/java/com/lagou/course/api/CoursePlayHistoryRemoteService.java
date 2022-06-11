package com.lagou.course.api;

import com.lagou.course.api.dto.CoursePlayHistoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName CoursePlayHistoryRemoteService
 * @Description 课程播放历史feign远程调用
 * @Author zhouqian
 * @Date 2022/6/6 18:29
 * @Version 1.0
 */
// @FeignClient(name = "${remote.feign.edu-course-boot.name:edu-course-boot}", path = "/course/coursePlayHistory")
@FeignClient(name = "edu-course-boot.CoursePlayHistoryRemoteService", path = "/course/coursePlayHistory")
public interface CoursePlayHistoryRemoteService {

    /**
     * 保存播放历史
     * @param playHistoryDTO
     */
    @PostMapping(value = "/saveCourse", consumes = "application/json")
    void saveCourseHistoryNode(@RequestBody CoursePlayHistoryDTO playHistoryDTO);

    /**
     * 获取播放的课程
     * @param userId
     * @param courseId
     * @return List
     */
    @GetMapping(value = "/hasStudyLessons", consumes = "application/json")
    List hasStudyLessons(@RequestParam("userId") Integer userId,
                         @RequestParam("courseId") Integer courseId);

    /**
     * 获取课程播放节点
     * @param lessonId
     * @param userId
     * @return CoursePlayHistoryDTO
     */
    @GetMapping(value = "/getByLessonId", consumes = "application/json")
    CoursePlayHistoryDTO getByLessonId(@RequestParam("lessonId") Integer lessonId,@RequestParam("userId") Integer userId);
}
