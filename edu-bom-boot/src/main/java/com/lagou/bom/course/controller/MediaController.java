package com.lagou.bom.course.controller;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.utils.StringUtils;
import com.lagou.bom.course.common.UserManager;
import com.lagou.common.response.ResponseDTO;
import com.lagou.common.result.ResultCode;
import com.lagou.course.api.MediaRemoteService;
import com.lagou.course.api.dto.MediaDTO;
import com.lagou.course.api.dto.VideoPlayDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Api(tags = "课程媒体接口")
@Slf4j
@RestController
@RequestMapping("/course/media")
public class MediaController {
    @Autowired
    private MediaRemoteService mediaRemoteService;
    @ApiOperation("通过课时ID获取媒体信息")
    @GetMapping("getByLessonId")
    public ResponseDTO getByLessonId(@RequestParam("lessonId") Integer lessonId) {
        try {
            MediaDTO mediaDTO = mediaRemoteService.getByLessonId(lessonId);
            if (mediaDTO == null) {
                return ResponseDTO.response(ResultCode.PARAM_ERROR.getState(), ResultCode.PARAM_ERROR.getMessage());
            }
            return ResponseDTO.success(mediaDTO);
        }catch (Exception e){
            return ResponseDTO.response(ResultCode.INTERNAL_ERROR.getState(), ResultCode.INTERNAL_ERROR.getMessage());
        }
    }

    @ApiOperation("获取阿里播放key")
    @GetMapping("alikey")
    public byte[] aliKey(HttpServletRequest request, String code, String vid) {
        Integer userId = UserManager.getUserId();
        // log.info("尝试获取阿里播放key:code:{}, vid:{},appId:{},UserId:{}", code, vid, userId);
        log.info("尝试获取阿里播放key:code:{}, vid:{},UserId:{}", code, vid, userId);
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(vid)){
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length ==  0){
            log.warn("检测到用户尚未进行登录处理，无法进行鉴权成功！code = {}, vid = {}", code, vid);
            return null;
        }
        byte[] dkBytes = null;
        try {
            dkBytes = mediaRemoteService.getCourseMediaDKByFileId(vid, code, userId);
            log.info("获取返回结果 dkBytes:{}", JSON.toJSONString(dkBytes));
        }catch (Exception e){
            log.error("获取视频鉴权失败！vid:{},code:{}", vid, code, e);
        }
        return dkBytes;
    }

    @ApiOperation("根据fileId获取阿里云对应的视频播放信息")
    @GetMapping("videoPlayInfo")
    public ResponseDTO videoPlayInfo(Integer lessonId){
        try {
            Integer userId = UserManager.getUserId();
            log.info("t根据fileId获取阿里云对应的视频播放信息 userId:{} lessonId:{}",userId,lessonId);
            VideoPlayDto videoPlayDto = mediaRemoteService.getVideoPlayInfo(lessonId, userId);
            return ResponseDTO.success(videoPlayDto);
        } catch (Exception e) {
            log.error("根据fileId获取阿里云对应的视频播放信息失败:", e);
            return ResponseDTO.response(ResultCode.INTERNAL_ERROR.getState(), ResultCode.INVALID_ACCESS.getMessage());
        }
    }
}
