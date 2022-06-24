package com.lagou.course.api;

import com.lagou.course.api.dto.SectionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName SectionRemoteService
 * @Description 章节Feign远程调用
 * @Author zhouqian
 * @Date 2022/6/6 18:27
 * @Version 1.0
 */
//@FeignClient(name = "${remote.feign.edu-course-boot.name:edu-course-boot}", path = "/course/section")
@FeignClient(name = "edu-course-boot-sectionRemoteService", path = "/course/section")
public interface SectionRemoteService {
    /**
     * 保存课程
     * @param sectionDTO
     * @return boolean
     */
    @PostMapping(value = "/saveOrUpdateSection", consumes = "application/json")
    boolean saveOrUpdateSection(@RequestBody SectionDTO sectionDTO);

    /**
     * 通过
     * @param courseId
     * @return List<SectionDTO>
     */
    @GetMapping(value = "/getSectionAndLesson")
    List<SectionDTO> getSectionAndLesson(@RequestParam("courseId") Integer courseId);

    /**
     * 获取章节信息
     * @param sectionId
     * @return SectionDTO
     */
    @GetMapping(value = "/getBySectionId", consumes = "application/json")
    SectionDTO getBySectionId(@RequestParam("sectionId") Integer sectionId);
}
