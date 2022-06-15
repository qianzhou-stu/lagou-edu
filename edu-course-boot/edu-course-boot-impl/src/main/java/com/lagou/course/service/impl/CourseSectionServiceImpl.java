package com.lagou.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.lagou.course.api.dto.LessonDTO;
import com.lagou.course.api.dto.SectionDTO;
import com.lagou.course.entity.CourseLesson;
import com.lagou.course.entity.CourseSection;
import com.lagou.course.mapper.CourseLessonMapper;
import com.lagou.course.mapper.CourseSectionMapper;
import com.lagou.course.service.ICourseSectionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName CourseSectionServiceImpl
 * @Description CourseSectionServiceImpl
 * @Author zhouqian
 * @Date 2022/6/8 11:00
 * @Version 1.0
 */
@Service
public class CourseSectionServiceImpl extends ServiceImpl<CourseSectionMapper, CourseSection> implements ICourseSectionService {

    @Autowired
    private CourseSectionMapper courseSectionMapper;
    @Autowired
    private CourseLessonMapper courseLessonMapper;

    /**
     * 保存和更新章节信息
     *
     * @param sectionDTO
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateSection(SectionDTO sectionDTO) {
        Integer sectionId = sectionDTO.getId();
        CourseSection courseSection = new CourseSection();
        BeanUtils.copyProperties(sectionDTO, courseSection);
        if (sectionId != null){
            // 更新的操作
            UpdateWrapper<CourseSection> courseSectionUpdateWrapper = new UpdateWrapper<>();
            courseSectionUpdateWrapper.eq("id", sectionId);
            int update = courseSectionMapper.update(courseSection, courseSectionUpdateWrapper);
            return update > 0;
        }else{
            // 插入的操作
            int insert = courseSectionMapper.insert(courseSection);
            return insert > 0;
        }
    }

    /**
     * 根据课程id得到对应的章节信息和课时信息
     *
     * @param courseId
     * @return List<SectionDTO>
     */
    @Override
    public List<SectionDTO> getSectionAndLesson(Integer courseId) {
        LambdaQueryWrapper<CourseSection> courseSectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseSectionLambdaQueryWrapper.eq(CourseSection::getCourseId, courseId);
        List<CourseSection> courseSections = courseSectionMapper.selectList(courseSectionLambdaQueryWrapper);
        List<SectionDTO> sectionDTOS = new ArrayList<>();
        courseSections.forEach((section)->{
            SectionDTO sectionDTO = new SectionDTO();
            BeanUtils.copyProperties(section, sectionDTO);
            LambdaQueryWrapper<CourseLesson> courseLessonLambdaQueryWrapper = new LambdaQueryWrapper<>();
            courseLessonLambdaQueryWrapper.eq(CourseLesson::getCourseId, courseId);
            courseLessonLambdaQueryWrapper.eq(CourseLesson::getSectionId, section.getId());
            List<CourseLesson> courseLessons = courseLessonMapper.selectList(courseLessonLambdaQueryWrapper);
            List<LessonDTO> lessonDTOS = new ArrayList<>();
            courseLessons.forEach(
                    courseLesson -> {
                        LessonDTO lessonDTO = new LessonDTO();
                        BeanUtils.copyProperties(courseLesson, lessonDTO);
                        lessonDTOS.add(lessonDTO);
                    }
            );
            sectionDTO.setLessonDTOS(lessonDTOS);
            sectionDTOS.add(sectionDTO);
        });
        return sectionDTOS;
    }

    /**
     * 根据sectionId获取章节信息
     *
     * @param sectionId
     * @return SectionDTO
     */
    @Override
    public SectionDTO getBySectionId(Integer sectionId) {
        CourseSection courseSection = courseSectionMapper.selectById(sectionId);
        SectionDTO sectionDTO = new SectionDTO();
        BeanUtils.copyProperties(courseSection, sectionDTO);
        return sectionDTO;
    }
}
