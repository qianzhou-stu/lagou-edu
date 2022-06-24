package com.lagou.boss.rest;

import com.lagou.boss.entity.form.LessonForm;
import com.lagou.boss.entity.vo.LessonVo;
import com.lagou.common.entity.vo.Result;
import com.lagou.common.response.EduEnum;
import com.lagou.common.util.ConvertUtil;
import com.lagou.course.api.LessonRemoteService;
import com.lagou.course.api.dto.LessonDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "课时内容", produces = "application/json")
@Slf4j
@RestController
@RequestMapping("/course/lesson")
public class LessonController {
    @Autowired
    private LessonRemoteService lessonRemoteService;

    @ApiOperation(value = "保存或更新课时")
    @PostMapping(value = "/saveOrUpdate")
    Result saveOrUpdate(@RequestBody LessonForm lessonForm){
        LessonDTO lessonDTO = ConvertUtil.convert(lessonForm, LessonDTO.class);
        boolean b = lessonRemoteService.saveOrUpdate(lessonDTO);
        if (b){
            return Result.success();
        }
        return Result.success(EduEnum.INSERT_OR_UPDATE_FAILURE);
    }

    @ApiOperation(value = "通过Id获取课时")
    @GetMapping(value = "/getById")
    Result getById(@RequestParam("lessonId") Integer lessonId){
        LessonDTO lessonDTO = lessonRemoteService.getById(lessonId);
        LessonVo lessonVo = ConvertUtil.convert(lessonDTO, LessonVo.class);
        return Result.success(lessonVo);
    }

}
