package com.lagou.comment.remote;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lagou.comment.entity.CourseComment;
import com.lagou.comment.entity.CourseCommentFavorite;
import com.lagou.comment.service.ICourseCommentFavoriteService;
import com.lagou.comment.service.ICourseCommentService;
import com.lagou.comment.util.EmojiCharacterConvertUtil;
import com.lagou.edu.comment.api.CourseCommentRemoteService;
import com.lagou.edu.comment.api.dto.CourseCommentDTO;
import com.lagou.edu.comment.api.param.CourseCommentParam;
import com.lagou.user.api.dto.UserDTO;
import com.lagou.user.api.feign.UserRemoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/comment")
public class CourseCommentService implements CourseCommentRemoteService {
    @Autowired
    private ICourseCommentService courseCommentService;
    @Autowired
    private ICourseCommentFavoriteService courseCommentFavoriteService;
    @Autowired
    private UserRemoteService userRemoteService;
    @Override
    @PostMapping(value = "/getCourseCommentList",consumes = "application/json")
    public List<CourseCommentDTO> getCourseCommentList(@RequestBody CourseCommentParam courseCommentParam) {
        log.info("获取评论 参数courseCommentParam：{}", JSON.toJSONString(courseCommentParam));
        Integer userId = courseCommentParam.getUserId();
        Integer courseId = courseCommentParam.getCourseId();
        int pageNum = courseCommentParam.getPageNum();
        int pageSize = courseCommentParam.getPageSize();
        Page<CourseComment> courseCommentList = courseCommentService.getCourseCommentList(courseId, pageNum, pageSize);
        List<CourseComment> courseComments = courseCommentList.getContent();
        if (courseComments == null || courseComments.isEmpty()) {
            return Collections.emptyList();
        }
        //获取一级留言的ID集合
        List<String> parentIds = new LinkedList<>();
        for(CourseComment comment:courseComments){
            parentIds.add(comment.getId());
        }
        //批量获取用户点赞的帖子  查询表获取到对应的点赞
        Map<String, Boolean> favoriteMapping = getFavoriteCourseCommentMap(userId,parentIds);

        List<CourseCommentDTO> userCommentList = new LinkedList<CourseCommentDTO>();

        // 遍历获取用户留言
        for (CourseComment courseComment : courseComments) {
            CourseCommentDTO courseCommentDTO = new CourseCommentDTO();
            BeanUtils.copyProperties(courseComment, courseCommentDTO);
            courseCommentDTO.setComment(convertCommentCharacter(courseComment.getComment()));
            if(favoriteMapping != null && !favoriteMapping.isEmpty()){
                Boolean favoriteTag = favoriteMapping.get(courseComment.getId());
                courseCommentDTO.setFavoriteTag(favoriteTag == null ? Boolean.FALSE:Boolean.TRUE);
            }
            if(Objects.equals(userId,courseComment.getUserId())){
                courseCommentDTO.setOwner(Boolean.TRUE);
            }
            UserDTO userDTO = userRemoteService.getUserById(courseComment.getUserId());
            if(userDTO != null){
                courseCommentDTO.setNickName(userDTO.getName());
            }
            userCommentList.add(courseCommentDTO);
        }
        log.info("返回的评论 userCommentList：{}",JSON.toJSONString(userCommentList));
        return userCommentList;
    }

    private String convertCommentCharacter(String comment) {
        try {
            return EmojiCharacterConvertUtil.emojiRecovery(comment);
        } catch (UnsupportedEncodingException e) {
            log.error("转换评论字符失败,comment={}", comment, e);
            return comment;
        }
    }

    private Map<String, Boolean> getFavoriteCourseCommentMap(Integer userId, List<String> parentIds) {
        if (CollectionUtils.isEmpty(parentIds)) {
            return Collections.emptyMap();
        }

        List<CourseCommentFavorite> favoriteRecords = courseCommentFavoriteService.getCommentFavoriteRecordList(userId, parentIds);
        if (CollectionUtils.isEmpty(favoriteRecords)) {
            return Collections.emptyMap();
        }

        Map<String, Boolean> favoriteMapping = new HashMap<>(favoriteRecords.size());
        for (CourseCommentFavorite record : favoriteRecords) {
            favoriteMapping.put(record.getCommentId(), Boolean.TRUE);
        }
        return favoriteMapping;
    }


    @Override
    public List<CourseCommentDTO> getMyCommentList(Integer userId, Integer courseId, Integer lessonId) {
        return null;
    }

    @Override
    public boolean saveCourseComment(CourseCommentDTO comment) {
        return courseCommentService.saveCourseComment(comment);
    }

    @Override
    public boolean deleteCourseComment(String commentId, Integer userId) {
        if (commentId == null || userId == null) {
            log.info("参数为空,commentId={},userId={}", commentId, userId);
            return false;
        }

        boolean deleteFlag = courseCommentService.updateCommentDelStatusByIdAndUserId(commentId, userId);
        if (!deleteFlag) {
            return false;
        }
        return true;
    }

    @Override
    public List<CourseCommentDTO> getUserById(Integer userId) {
        return null;
    }
}
