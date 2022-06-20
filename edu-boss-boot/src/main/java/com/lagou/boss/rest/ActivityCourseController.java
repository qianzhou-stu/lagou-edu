package com.lagou.boss.rest;


import com.alibaba.fastjson.JSON;
import com.lagou.boss.entity.form.ActivityCourseForm;
import com.lagou.common.entity.vo.Result;
import com.lagou.common.response.ResponseDTO;
import com.lagou.common.util.ConvertUtil;
import com.lagou.common.util.ValidateUtils;
import com.lagou.course.api.ActivityCourseRemoteService;
import com.lagou.course.api.dto.ActivityCourseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "活动课程", produces = "application/json")
@Slf4j
@RestController
@RequestMapping("/activityCourse")
public class ActivityCourseController {

    @Autowired
    private ActivityCourseRemoteService activityCourseRemoteService;

    @ApiOperation("保存活动商品")
    @PostMapping("/save")
    public Result<?> save(@RequestBody ActivityCourseForm reqVo) {
        log.info("save - reqVo:{}", JSON.toJSONString(reqVo));
        ActivityCourseDTO activityCourseDTO = ConvertUtil.convert(reqVo, ActivityCourseDTO.class);
        ResponseDTO<?> resp = activityCourseRemoteService.saveActivityCourse(activityCourseDTO);
        log.info("save - activityCourseRemoteService.saveActivityCourse - resp：{}",JSON.toJSONString(resp));
        ValidateUtils.isTrue(resp.isSuccess(), resp.getMessage());
        return Result.success();
    }

    @ApiOperation("更新活动商品状态")
    @PostMapping("/updateStatus")
    public Result<?> updateStatus(@RequestBody ActivityCourseForm reqVo) {
        log.info("updateStatus - reqVo:{}", JSON.toJSONString(reqVo));
        ResponseDTO<?> resp = activityCourseRemoteService.updateActivityCourseStatus(ConvertUtil.convert(reqVo, ActivityCourseDTO.class));
        log.info("updateStatus - activityCourseRemoteService.updateActivityCourseStatus - resp：{}",JSON.toJSONString(resp));
        ValidateUtils.isTrue(resp.isSuccess(), resp.getMessage());
        return Result.success();
    }
}
