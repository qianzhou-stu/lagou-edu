package com.lagou.bom.course.service;

import com.lagou.bom.course.model.response.CoursePurchasedRecordRespVo;

import java.util.List;

public interface CourseService {
    List<CoursePurchasedRecordRespVo> getAllCoursePurchasedRecord(Integer userId);
}
