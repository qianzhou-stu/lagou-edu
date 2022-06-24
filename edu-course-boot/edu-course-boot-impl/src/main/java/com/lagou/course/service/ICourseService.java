package com.lagou.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.course.api.dto.CourseDTO;
import com.lagou.course.api.dto.PageResultDTO;
import com.lagou.course.api.dto.SectionDTO;
import com.lagou.course.api.param.CourseQueryParam;
import com.lagou.course.entity.Course;

import java.util.List;

public interface ICourseService extends IService<Course> {
    /**
     * 获取选课列表
     * @param userId
     * @return List<CourseDTO>
     */
    List<CourseDTO> getAllCourses(Integer userId);

    /**
     * 获取已购课程信息
     * @param userId
     * @return List<CourseDTO>
     */
    List<CourseDTO> getPurchasedCourse(Integer userId);

    /**
     * 获取课程详情
     * @param courseId
     * @return CourseDTO
     */
    CourseDTO getCourseById(Integer courseId, Integer userId);


    /**
     * 更新课程
     * @param courseDTO
     * @return boolean
     */
    boolean saveOrUpdateCourse(CourseDTO courseDTO);

    /**
     * 根据条件查询课程信息
     * @param courseQueryParam
     * @return PageResultDTO<CourseDTO>
     */
    PageResultDTO<CourseDTO> getQueryCourses(CourseQueryParam courseQueryParam);

    Boolean changeState(Integer courseId, Integer status);

    /**
     * 根据配置的自动上架时间，定时任务扫描达到上架时间的草稿状态的课程进行上架。
     */
    void courseAutoOnline();
}
