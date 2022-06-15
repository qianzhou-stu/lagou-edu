package com.lagou.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.course.api.dto.TeacherDTO;
import com.lagou.course.entity.Teacher;
import com.lagou.course.mapper.TeacherMapper;
import com.lagou.course.service.ITeacherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TeacherMapper teacherMapper;
    @Override
    public TeacherDTO getTeacherByCourseId(Integer courseId) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        Teacher teacher = teacherMapper.selectList(queryWrapper).get(0);
        TeacherDTO teacherDTO = new TeacherDTO();
        BeanUtils.copyProperties(teacher, teacherDTO);
        return teacherDTO;
    }
}
