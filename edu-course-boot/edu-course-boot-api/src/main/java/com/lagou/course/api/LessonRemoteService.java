package com.lagou.course.api;

import com.lagou.course.api.dto.LessonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @ClassName LessonRemoteService
 * @Description 课时Feign远程调用
 * @Author zhouqian
 * @Date 2022/6/6 18:28
 * @Version 1.0
 */
@FeignClient(name = "${remote.feign.edu-course-boot.name:edu-course-boot}", path = "/course/lesson")
public interface LessonRemoteService {

    /**
     * 保存或者更新课程
     *
     * @param lessonDTO
     * @return boolean
     */
    @PostMapping(value = "/saveOrUpdate", consumes = "application/json")
    boolean saveOrUpdate(@RequestBody LessonDTO lessonDTO);

    /**
     * 通过lessonId获取课时
     *
     * @param lessonId
     * @return LessonDTO
     */
    @GetMapping(value = "/getById")
    LessonDTO getById(@RequestParam("lessonId") Integer lessonId);

    /**
     * 通过lessonId获取对应课时名称，map
     *
     * @param lessonIds
     * @return Map<Integer, String>
     */
    @GetMapping(value = "/getByIds")
    Map<Integer, String> getByIds(@RequestParam("lessonIds") List<Integer> lessonIds);

    /**
     * 得到发布课程
     * @param courseId
     * @return Integer
     */
    @GetMapping("/getReleaseCourse")
    Integer getReleaseCourse(Integer courseId);

    @GetMapping("/getBySectionId")
    public List<LessonDTO> getBySectionId(Integer sectionId);
}
