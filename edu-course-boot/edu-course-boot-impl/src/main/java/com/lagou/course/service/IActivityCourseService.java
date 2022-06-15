package com.lagou.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.course.api.dto.ActivityCourseDTO;
import com.lagou.course.entity.ActivityCourse;

public interface IActivityCourseService extends IService<ActivityCourse> {
    Boolean saveActivityCourse(ActivityCourseDTO reqDTO);

    Boolean updateActivityCourseStatus(ActivityCourseDTO reqDTO);

    ActivityCourseDTO getByCourseId(Integer courseId);

    Boolean updateActivityCourseStock(Integer courseId, String orderNo);
}
