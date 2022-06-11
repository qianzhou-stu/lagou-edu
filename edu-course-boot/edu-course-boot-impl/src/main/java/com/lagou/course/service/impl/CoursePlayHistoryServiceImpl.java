package com.lagou.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.lagou.course.api.dto.CoursePlayHistoryDTO;
import com.lagou.course.entity.CoursePlayHistory;
import com.lagou.course.mapper.CoursePlayHistoryMapper;
import com.lagou.course.service.ICoursePlayHistoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName CoursePlayHistoryServiceImpl
 * @Description CoursePlayHistoryServiceImpl
 * @Author zhouqian
 * @Date 2022/6/8 10:53
 * @Version 1.0
 */
@Service
public class CoursePlayHistoryServiceImpl extends ServiceImpl<CoursePlayHistoryMapper, CoursePlayHistory> implements ICoursePlayHistoryService {

    @Autowired
    private CoursePlayHistoryMapper coursePlayHistoryMapper;

    @Override
    public CoursePlayHistoryDTO getRecordLearn(Integer courseId, Integer userId) {
        // 获取学习记录
        QueryWrapper<CoursePlayHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_del", Boolean.FALSE);
        queryWrapper.orderByDesc("create_time");
        List<CoursePlayHistory> coursePlayHistories = coursePlayHistoryMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(coursePlayHistories)){
            return null;
        }
        CoursePlayHistory coursePlayHistory = coursePlayHistories.get(0);
        CoursePlayHistoryDTO coursePlayHistoryDTO = new CoursePlayHistoryDTO();
        BeanUtils.copyProperties(coursePlayHistory, coursePlayHistoryDTO);
        return coursePlayHistoryDTO;
    }

    @Override
    public CoursePlayHistoryDTO getByLessonId(Integer lessonId, Integer userId) {
        return null;
    }

    @Override
    public CoursePlayHistoryDTO getByUserIdAndCourseId(Integer userId, Integer courseId) {
        return null;
    }

    @Override
    public List<Integer> hasStudyLessons(Integer userId, Integer courseId) {
        return null;
    }

    @Override
    public void saveCourseHistoryNode(CoursePlayHistoryDTO playHistoryDTO) {

    }
}
