package com.lagou.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.course.api.dto.MediaDTO;
import com.lagou.course.entity.CourseMedia;

public interface ICourseMediaService extends IService<CourseMedia> {

    MediaDTO getByLessonId(Integer lessonId);

    void updateOrSaveMedia(MediaDTO mediaDTO);
}
