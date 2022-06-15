package com.lagou.course.remote;

import com.lagou.course.api.SectionRemoteService;
import com.lagou.course.api.dto.SectionDTO;
import com.lagou.course.service.ICourseSectionService;
import com.lagou.course.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName CourseSectionController
 * @Description CourseSectionController实现远程调用类
 * @Author zhouqian
 * @Date 2022/6/8 11:03
 * @Version 1.0
 */
@RestController(value = "/course/section")
public class CourseSectionController implements SectionRemoteService {
    @Autowired
    private ICourseSectionService courseSectionService;

    /**
     * 保存和更新章节信息
     * @param sectionDTO
     * @return boolean
     */
    @Override
    @PostMapping(value = "/saveOrUpdateSection", consumes = "application/json")
    public boolean saveOrUpdateSection(@RequestBody SectionDTO sectionDTO) {
        return courseSectionService.saveOrUpdateSection(sectionDTO);
    }

    /**
     * 根据课程id得到对应的章节信息和课时信息
     * @param courseId
     * @return List<SectionDTO>
     */
    @Override
    @GetMapping(value = "/getSectionAndLesson")
    public List<SectionDTO> getSectionAndLesson(@RequestParam("courseId") Integer courseId) {
        return courseSectionService.getSectionAndLesson(courseId);
    }

    /**
     * 根据sectionId获取章节信息
     * @param sectionId
     * @return SectionDTO
     */
    @Override
    @GetMapping(value = "/getBySectionId", consumes = "application/json")
    public SectionDTO getBySectionId(@RequestParam("sectionId") Integer sectionId) {
        return courseSectionService.getBySectionId(sectionId);
    }
}
