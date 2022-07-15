package com.lagou.boss.service;

import com.lagou.course.api.dto.LessonDTO;

public interface ILessonService {
    boolean saveOrUpdate(LessonDTO lessonDTO);
}
