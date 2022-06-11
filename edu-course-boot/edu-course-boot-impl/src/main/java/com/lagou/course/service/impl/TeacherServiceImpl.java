package com.lagou.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.course.entity.Teacher;
import com.lagou.course.mapper.TeacherMapper;
import com.lagou.course.service.ITeacherService;
import org.springframework.stereotype.Service;

/**
 * @ClassName TeacherServiceImpl
 * @Description TeacherServiceImpl实现类
 * @Author zhouqian
 * @Date 2022/6/8 09:03
 * @Version 1.0
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher>  implements ITeacherService {

}
