package com.lagou.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.course.api.dto.SectionDTO;
import com.lagou.course.entity.CourseSection;

import java.util.List;

public interface ICourseSectionService extends IService<CourseSection> {
    /**
     * 保存和更新章节信息
     * @param sectionDTO
     * @return boolean
     */
    boolean saveOrUpdateSection(SectionDTO sectionDTO);

    /**
     * 根据课程id得到对应的章节信息和课时信息
     * @param courseId
     * @return List<SectionDTO>
     */
    List<SectionDTO> getSectionAndLesson(Integer courseId);

    /**
     * 根据sectionId获取章节信息
     * @param sectionId
     * @return SectionDTO
     */
    SectionDTO getBySectionId(Integer sectionId);
}
