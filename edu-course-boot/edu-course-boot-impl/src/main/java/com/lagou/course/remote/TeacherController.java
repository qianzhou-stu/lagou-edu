package com.lagou.course.remote;

import com.lagou.course.api.TeacherRemoteService;
import com.lagou.course.api.dto.TeacherDTO;
import com.lagou.course.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName TeacherController
 * @Description TeacherController远程调用接口
 * @Author zhouqian
 * @Date 2022/6/8 11:05
 * @Version 1.0
 */

public class TeacherController implements TeacherRemoteService {

    @Autowired
    private ITeacherService teacherService;
    /**
     * 根据课程的id信息查询对应的教师信息
     * @param courseId
     * @return
     */
    @Override
    @GetMapping(value = "/getTeacherByCourseId", consumes = "application/json")
    public TeacherDTO getTeacherByCourseId(Integer courseId) {
        return teacherService.getTeacherByCourseId(courseId);
    }
}
