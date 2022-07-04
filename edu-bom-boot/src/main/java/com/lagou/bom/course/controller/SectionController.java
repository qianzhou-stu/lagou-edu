package com.lagou.bom.course.controller;

import com.lagou.bom.common.UserManager;
import com.lagou.bom.course.model.response.CourseSectionListResult;
import com.lagou.bom.course.service.SectionService;
import com.lagou.common.response.ResponseDTO;
import com.lagou.common.result.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "课程章节接口")
@Slf4j
@RestController
@RequestMapping("/course/session")
public class SectionController {
    @Autowired
    private SectionService sectionService;
    /**
     * 通过课程Id获取章节和课时
     *
     * @param courseId
     * @return
     */
    @ApiOperation(value = "获取课程章节", produces = MimeTypeUtils.APPLICATION_JSON_VALUE, notes = "获取课程章节接口")
    @GetMapping(value = "/getSectionAndLesson")
    ResponseDTO getSectionAndLesson(@RequestParam("courseId") Integer courseId) {
        try{
            Integer userId = UserManager.getUserId();
            CourseSectionListResult courseSectionListResult = sectionService.getSectionInfoByCourseId(userId, courseId);
            return ResponseDTO.success(courseSectionListResult);
        }catch (Exception e){
            log.error("通过课程Id获取章节和课时 失败", e);
            return ResponseDTO.response(ResultCode.INTERNAL_ERROR.getState(), ResultCode.INTERNAL_ERROR.getMessage());
        }
    }
}
