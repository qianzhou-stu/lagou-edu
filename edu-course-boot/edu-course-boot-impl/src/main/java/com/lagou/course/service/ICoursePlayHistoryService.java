package com.lagou.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.course.api.dto.CoursePlayHistoryDTO;
import com.lagou.course.entity.CoursePlayHistory;

import java.util.List;

/**
 * @ClassName ICoursePlayHistoryService
 * @Description ICoursePlayHistoryService 播放历史service
 * @Author zhouqian
 * @Date 2022/6/8 10:52
 * @Version 1.0
 */
public interface ICoursePlayHistoryService extends IService<CoursePlayHistory> {
    public CoursePlayHistoryDTO getRecordLearn(Integer courseId, Integer userId);

    public CoursePlayHistoryDTO getByLessonId(Integer lessonId, Integer userId);

    public CoursePlayHistoryDTO getByUserIdAndCourseId(Integer userId, Integer courseId);

    public List<Integer> hasStudyLessons(Integer userId, Integer courseId);

    public void saveCourseHistoryNode(CoursePlayHistoryDTO playHistoryDTO);
}
