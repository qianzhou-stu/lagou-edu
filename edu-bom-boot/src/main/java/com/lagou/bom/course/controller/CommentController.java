package com.lagou.bom.course.controller;

import com.alibaba.fastjson.JSON;
import com.lagou.bom.course.model.request.CommentReq;
import com.lagou.bom.course.model.response.CourseCommentRespVo;
import com.lagou.common.response.ResponseDTO;
import com.lagou.common.result.ResultCode;
import com.lagou.edu.comment.api.CourseCommentFavoriteRemoteService;
import com.lagou.edu.comment.api.CourseCommentRemoteService;
import com.lagou.edu.comment.api.dto.CourseCommentDTO;
import com.lagou.edu.comment.api.param.CourseCommentParam;
import com.lagou.front.common.UserManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "评论", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping("/comment/")
public class CommentController {
    @Autowired
    private CourseCommentRemoteService courseCommentRemoteService;
    @Autowired
    private CourseCommentFavoriteRemoteService courseCommentFavoriteRemoteService;
    /**
     * 获取课程或课时下的用户评论,
     *
     * @return
     */
    @ApiOperation(value = "获取评论列表")
    @PostMapping("/getCourseCommentList")
    ResponseDTO getCourseCommentList(@RequestBody CourseCommentParam courseCommentParam){
        try {
            courseCommentParam.setUserId(UserManager.getUserId());
            List<CourseCommentDTO> courseCommentList = courseCommentRemoteService.getCourseCommentList(courseCommentParam);
            if (CollectionUtils.isEmpty(courseCommentList)){
                return ResponseDTO.response(ResultCode.SUCCESS.getState(), null, null);
            }
            List<CourseCommentRespVo> courseCommentRespVos = courseCommentList.stream().map(courseCommentDTO -> {
                CourseCommentRespVo courseCommentRespVo = new CourseCommentRespVo();
                BeanUtils.copyProperties(courseCommentDTO, courseCommentRespVo);
                return courseCommentRespVo;
            }).collect(Collectors.toList());
            return ResponseDTO.response(ResultCode.SUCCESS.getState(), null, courseCommentRespVos);
        }catch (Exception e) {
            log.error("获取评论列表失败:", e);
            return ResponseDTO.response(ResultCode.INTERNAL_ERROR.getState(), "获取数据失败", null);
        }
    }

    @ApiOperation(value = "保存评论")
    @PostMapping(value = "/saveCourseComment")
    ResponseDTO saveCourseComment(@RequestBody CommentReq commentReq){
        try {
            Integer userId = UserManager.getUserId();
            CourseCommentDTO courseCommentDTO = new CourseCommentDTO();
            BeanUtils.copyProperties(commentReq,courseCommentDTO);
            courseCommentDTO.setUserId(userId);
            log.info("保存评论 commentDTO：{}", JSON.toJSONString(courseCommentDTO));
            courseCommentRemoteService.saveCourseComment(courseCommentDTO);
            return ResponseDTO.response(ResultCode.SUCCESS.getState(), ResultCode.SUCCESS.getMessage());
        }catch (Exception e){
            log.error("保存评论失败:", e);
            return ResponseDTO.response(ResultCode.INTERNAL_ERROR.getState(), "获取数据失败", null);
        }
    }
    /**
     * 逻辑删除课程评论
     *
     * @param commentId
     * @return
     */
    @ApiOperation(value = "删除评论")
    @GetMapping("/deleteCourseComment")
    ResponseDTO deleteCourseComment(@RequestParam("commentId") String commentId){
        try{
            Integer userId = UserManager.getUserId();
            courseCommentRemoteService.deleteCourseComment(commentId, userId);
            return ResponseDTO.response(ResultCode.SUCCESS.getState(), ResultCode.SUCCESS.getMessage());
        }catch (Exception e){
            log.error("删除评论 失败:", e);
            return ResponseDTO.response(ResultCode.INTERNAL_ERROR.getState(), "获取数据失败", null);
        }
    }
    @ApiOperation(value = "点赞")
    @GetMapping("/favorite")
    ResponseDTO favorite(@RequestParam("commentId") String commentId){
        try {
            Integer userId = UserManager.getUserId();
            courseCommentFavoriteRemoteService.favorite(userId,commentId);
            return ResponseDTO.response(ResultCode.SUCCESS.getState(), ResultCode.SUCCESS.getMessage());
        } catch (Exception e) {
            log.error("点赞失败:", e);
            return ResponseDTO.response(ResultCode.INTERNAL_ERROR.getState(), "获取数据失败", null);
        }

    }
    @ApiOperation(value = "取消点赞")
    @GetMapping("/cancelFavorite")
    ResponseDTO cancelFavorite(@RequestParam("commentId") String commentId){
        try {
            Integer userId = UserManager.getUserId();
            courseCommentFavoriteRemoteService.cancelFavorite(userId, commentId);
        } catch (Exception e) {
            log.error("取消点赞失败:", e);
            return ResponseDTO.response(ResultCode.INTERNAL_ERROR.getState(), "获取数据失败", null);
        }
        return ResponseDTO.response(ResultCode.SUCCESS.getState(), ResultCode.SUCCESS.getMessage());

    }

}
