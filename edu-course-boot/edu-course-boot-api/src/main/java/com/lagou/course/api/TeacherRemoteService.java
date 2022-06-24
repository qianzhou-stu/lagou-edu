package com.lagou.course.api;

import com.lagou.course.api.dto.TeacherDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName TeacherRemoteService
 * @Description 教师Feign远程调用
 * @Author zhouqian
 * @Date 2022/6/6 18:26
 * @Version 1.0
 */
@FeignClient(name = "${remote.feign.edu-course-boot.name:edu-course-boot}", path = "/teacher")
public interface TeacherRemoteService {
    /**
     * 通过课程Id获取老师信息
     * @param courseId
     * @return TeacherDTO
     */
    @GetMapping(value = "/getTeacherByCourseId", consumes = "application/json")
    TeacherDTO getTeacherByCourseId(Integer courseId);
}
