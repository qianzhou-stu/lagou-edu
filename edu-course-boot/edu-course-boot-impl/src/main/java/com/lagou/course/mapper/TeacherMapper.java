package com.lagou.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lagou.course.entity.Teacher;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherMapper extends BaseMapper<Teacher> {

    Teacher selectByCourseId(Integer id);
}