package com.lagou.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.course.entity.CourseLesson;
import com.lagou.course.entity.CourseMedia;
import com.lagou.course.mapper.CourseLessonMapper;
import com.lagou.course.mapper.CourseMediaMapper;
import com.lagou.course.service.ICourseMediaService;
import com.lagou.course.service.ILessonService;
import org.springframework.stereotype.Service;

/**
 * @ClassName LessonServiceImpl
 * @Description CourseMediaServiceImpl实现类
 * @Author zhouqian
 * @Date 2022/6/8 09:09
 * @Version 1.0
 */
@Service
public class CourseMediaServiceImpl extends ServiceImpl<CourseMediaMapper, CourseMedia> implements ICourseMediaService {

}
