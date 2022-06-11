package com.lagou.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.course.entity.CourseSection;
import com.lagou.course.mapper.CourseSectionMapper;
import com.lagou.course.service.ICourseSectionService;
import org.springframework.stereotype.Service;

/**
 * @ClassName CourseSectionServiceImpl
 * @Description CourseSectionServiceImpl
 * @Author zhouqian
 * @Date 2022/6/8 11:00
 * @Version 1.0
 */
@Service
public class CourseSectionServiceImpl extends ServiceImpl<CourseSectionMapper, CourseSection> implements ICourseSectionService {
}
