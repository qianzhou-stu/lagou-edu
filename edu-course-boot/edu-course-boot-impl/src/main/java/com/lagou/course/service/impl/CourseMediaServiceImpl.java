package com.lagou.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.course.api.dto.MediaDTO;
import com.lagou.course.entity.CourseLesson;
import com.lagou.course.entity.CourseMedia;
import com.lagou.course.mapper.CourseLessonMapper;
import com.lagou.course.mapper.CourseMediaMapper;
import com.lagou.course.service.ICourseMediaService;
import com.lagou.course.service.ILessonService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private CourseMediaMapper courseMediaMapper;

    @Override
    public MediaDTO getByLessonId(Integer lessonId) {
        LambdaQueryWrapper<CourseMedia> courseMediaLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseMediaLambdaQueryWrapper.eq(CourseMedia::getLessonId, lessonId);
        CourseMedia courseMedia = courseMediaMapper.selectOne(courseMediaLambdaQueryWrapper);
        MediaDTO mediaDTO = new MediaDTO();
        BeanUtils.copyProperties(courseMedia, mediaDTO);
        return mediaDTO;
    }

    @Override
    public void updateOrSaveMedia(MediaDTO mediaDTO) {
        if (mediaDTO.getId() != null){
            // 更新
            CourseMedia media = new CourseMedia();
            BeanUtils.copyProperties(mediaDTO, media);
            courseMediaMapper.updateById(media);
        }else {
            // 新增
            CourseMedia media = new CourseMedia();
            BeanUtils.copyProperties(mediaDTO, media);
            courseMediaMapper.insert(media);
        }
    }
}
