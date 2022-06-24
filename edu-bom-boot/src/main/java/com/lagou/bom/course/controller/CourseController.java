package com.lagou.bom.course.controller;

import com.lagou.bom.course.model.response.CoursePurchasedRecordRespVo;
import com.lagou.bom.course.model.response.CourseResp;
import com.lagou.bom.course.service.CourseService;
import com.lagou.common.entity.vo.Result;
import com.lagou.common.exception.SystemErrorType;
import com.lagou.common.response.EduEnum;
import com.lagou.common.response.ResponseDTO;
import com.lagou.common.result.ResultCode;
import com.lagou.course.api.CoursePlayHistoryRemoteService;
import com.lagou.course.api.CourseRemoteService;
import com.lagou.course.api.dto.CourseDTO;
import com.lagou.course.api.dto.PageResultDTO;
import com.lagou.course.api.param.CourseQueryParam;
import com.lagou.front.common.UserManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "课程接口", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
@RequestMapping("/course/")
public class CourseController {
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private CoursePlayHistoryRemoteService coursePlayHistoryRemoteService;
    @Autowired
    private CourseService courseService;
    @ApiOperation(value = "获取选课内容")
    @RequestMapping(value = "getAllCourse",method= RequestMethod.GET)
    public ResponseDTO<List<CourseResp>> getAllCourse(){
        try {
            Integer userId = UserManager.getUserId();
            List<CourseDTO> allCourses = this.courseRemoteService.getAllCourses(userId);
            List<CourseResp> courseResps = allCourses.stream()
                    .map(courseDTO -> {
                        CourseResp courseResp = new CourseResp();
                        BeanUtils.copyProperties(courseDTO, courseResp);
                        return courseResp;
                    }).collect(Collectors.toList());
            return ResponseDTO.response(ResultCode.SUCCESS.getState(), null, courseResps);
        }catch (Exception e){
            log.error("获取选课失败:", e);
            return ResponseDTO.response(ResultCode.INTERNAL_ERROR.getState(), "获取数据失败", null);
        }
    }

    @ApiOperation(value = "获取课程详情")
    @RequestMapping(value = "getCourseById", method = RequestMethod.GET)
    public ResponseDTO getCourseById(@RequestParam("courseId") Integer courseId){
        try{
            Integer userId = UserManager.getUserId();
            CourseDTO courseDTO = this.courseRemoteService.getCourseById(courseId, userId);
            if (courseDTO == null){
                return ResponseDTO.response(ResultCode.SUCCESS.getState()
                        ,ResultCode.SUCCESS.getMessage(), null);
            }
            CourseResp courseResp = new CourseResp();
            BeanUtils.copyProperties(courseDTO, courseResp);
            return ResponseDTO.response(ResultCode.SUCCESS.getState(),ResultCode.SUCCESS.getMessage(), courseResp);
        }catch (Exception e){
            log.error("获取课程详情失败:", e);
            return ResponseDTO.response(ResultCode.INTERNAL_ERROR.getState(), "获取数据失败");
        }
    }

    @ApiOperation(value = "获取已购课程")
    @RequestMapping(value = "getPurchaseCourse",method=RequestMethod.GET)
    public ResponseDTO getPurchaseCourse(){
        try{
            Integer userId = UserManager.getUserId();
            if(userId == null){
                log.info("[获取已购课程] 用户ID为空，获取数据为空");
                return ResponseDTO.response(ResultCode.SUCCESS.getState(),ResultCode.SUCCESS.getMessage());
            }
            log.info("获取已购课程 userId:{}" ,userId);
            List<CoursePurchasedRecordRespVo>  coursePurchasedRecordRespVos= courseService.getAllCoursePurchasedRecord(userId);
            return ResponseDTO.response(ResultCode.SUCCESS.getState(), null, coursePurchasedRecordRespVos);
        }catch (Exception e){
            log.error("获取已购课程:", e);
            return ResponseDTO.response(ResultCode.INTERNAL_ERROR.getState(), "获取数据失败", null);
        }
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("getQueryCourses")
    public ResponseDTO getQueryCourses(@RequestBody CourseQueryParam courseQueryParam) {
        PageResultDTO<CourseDTO> pageResultDTO = this.courseRemoteService.getQueryCourses(courseQueryParam);
        return ResponseDTO.response(1, null, pageResultDTO);
    }
}
