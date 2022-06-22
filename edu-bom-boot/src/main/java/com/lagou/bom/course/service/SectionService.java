package com.lagou.bom.course.service;

import com.lagou.bom.course.model.response.CourseSectionListResult;

public interface SectionService {
    CourseSectionListResult getSectionInfoByCourseId(Integer userId, Integer courseId);
}
