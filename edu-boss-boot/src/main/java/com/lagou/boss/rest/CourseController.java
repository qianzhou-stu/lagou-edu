package com.lagou.boss.rest;

import com.lagou.boss.entity.bo.UpLoadResult;
import com.lagou.boss.entity.form.CourseForm;
import com.lagou.boss.entity.vo.CourseVo;
import com.lagou.boss.service.OssService;
import com.lagou.common.entity.vo.Result;
import com.lagou.common.response.EduEnum;
import com.lagou.common.util.ConvertUtil;
import com.lagou.course.api.CourseRemoteService;
import com.lagou.course.api.dto.CourseDTO;
import com.lagou.course.api.dto.PageResultDTO;
import com.lagou.course.api.param.CourseQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "课程", produces = "application/json")
@Slf4j
@RestController
@RequestMapping("/course/")
public class CourseController {

    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private OssService ossService;


    @ApiOperation(value = "保存或者更新课程信息")
    @PostMapping("saveOrUpdateCourse")
    public Result saveOrUpdateCourse(@RequestBody CourseForm courseForm){
        CourseDTO courseDTO = ConvertUtil.convert(courseForm, CourseDTO.class);
        boolean b = courseRemoteService.saveOrUpdateCourse(courseDTO);
        if (b){
            return Result.success();
        }
        return Result.fail(EduEnum.INSERT_OR_UPDATE_FAILURE);
    }

    @ApiOperation(value = "通过课程Id获取课程信息")
    @GetMapping("getCourseById")
    public Result<CourseVo> getCourseById(@RequestParam("courseId") Integer courseId){
        CourseDTO courseDTO = courseRemoteService.getCourseById(courseId, null);
        CourseVo courseVo = ConvertUtil.convert(courseDTO, CourseVo.class);
        return Result.success(courseVo);
    }

    @ApiOperation(value = "课程上下架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "courseId", value = "课程ID"),
            @ApiImplicitParam(name = "status", value = "课程状态，0-草稿，1-上架")
    })
    @GetMapping("changeState")
    public Result changeState(@RequestParam("courseId") Integer courseId,
                              @RequestParam("status") Integer status){
        return courseRemoteService.changeState(courseId, status);
    }

    @ApiOperation(value = "分页查询课程信息")
    @PostMapping("getQueryCourses")
    public Result getQueryCourses(@RequestBody CourseQueryParam courseQueryParam)  {
        PageResultDTO<CourseDTO> queryCourses = courseRemoteService.getQueryCourses(courseQueryParam);
        return Result.success(queryCourses);
    }

    @ApiOperation(value = "上传图片")
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile multipartFile) {
        UpLoadResult upLoadResult = ossService.upload(multipartFile);
        return Result.success(upLoadResult);
    }

}
