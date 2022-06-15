package com.lagou.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.course.api.dto.TeacherDTO;
import com.lagou.course.entity.Teacher;

public interface ITeacherService extends IService<Teacher> {

    TeacherDTO getTeacherByCourseId(Integer courseId);
}
