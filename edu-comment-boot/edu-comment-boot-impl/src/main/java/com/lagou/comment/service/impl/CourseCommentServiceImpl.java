package com.lagou.comment.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lagou.comment.entity.CourseComment;
import com.lagou.comment.repository.CourseCommentRepository;
import com.lagou.comment.service.ICourseCommentService;
import com.lagou.comment.util.EmojiCharacterConvertUtil;
import com.lagou.edu.comment.api.dto.CourseCommentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CourseCommentServiceImpl implements ICourseCommentService {
    @Autowired
    private CourseCommentRepository courseCommentRepository;

    @Override
    public Page<CourseComment> getCourseCommentList(Integer courseId, int pageNum, int pageSize) {
        if (courseId == null){
            return null;
        }
        if (pageNum < 1){
            pageNum = 0;
        }
        // 分页查询课程对应的评论列表
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.Direction.DESC, "createTime", "likeCount");
        CourseComment courseComment = new CourseComment();
        courseComment.setCourseId(courseId);
        courseComment.setIsDel(Boolean.FALSE);
        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("courseId", ExampleMatcher.GenericPropertyMatchers.exact());
        Example<CourseComment> example = Example.of(courseComment, matcher);
        return courseCommentRepository.findAll(example, pageable);
    }

    @Override
    public boolean saveCourseComment(CourseCommentDTO comment) {
        if (Objects.isNull(comment)){
            log.error("comment 参数为空");
            return false;
        }
        try {
            comment.setComment(EmojiCharacterConvertUtil.emojiConvertString(comment.getComment()));
            CourseComment courseComment = new CourseComment();
            CourseComment insert = courseCommentRepository.insert(courseComment);
            if(insert != null){
                return true;
            }
            return false;
        } catch (UnsupportedEncodingException e) {
            log.error("保存评论信息异常，courseId = {}, err = {}", comment.getCourseId(), e);
            return false;
        }
    }

    @Override
    public boolean updateCommentDelStatusByIdAndUserId(String commentId, Integer userId) {
        CourseComment courseComment = new CourseComment();
        courseComment.setId(commentId);
        courseComment.setIsDel(Boolean.FALSE);
        courseComment.setUserId(userId);
        //创建匹配器，组装查询条件
        ExampleMatcher matcher = ExampleMatcher.matching();
        //创建实例
        Example<CourseComment> ex = Example.of(courseComment, matcher);
        List<CourseComment> courseComments = courseCommentRepository.findAll(ex);
        if(CollectionUtils.isEmpty(courseComments)){
            log.error("获取的评论为空 commentId:{} userId:{}",commentId,userId);
            return false;
        }
        CourseComment courseCommentNew = courseComments.get(0);
        courseCommentNew.setIsDel(Boolean.TRUE);
        courseCommentRepository.save(courseCommentNew);
        return Boolean.TRUE;
    }

    @Override
    public void updateNumOfLike(String commentId, Boolean flag) {
        CourseComment courseComment = new CourseComment();
        courseComment.setId(commentId);
        List<CourseComment> courseComments = courseCommentRepository.findAll(Example.of(courseComment, ExampleMatcher.matching()));
        if(CollectionUtils.isEmpty(courseComments)){
            log.error("评论id没有对应的数据 commentId:{}",commentId);
            return;
        }
        CourseComment comment = courseComments.get(0);
        Integer likeCount = comment.getLikeCount();
        if(flag){
            likeCount++;
        }else{
            likeCount--;
        }
        log.info("更新点赞 likeCount：{} flag:{} 以前点赞数量：{}",likeCount ,flag ,comment.getLikeCount());
        comment.setLikeCount(likeCount);
        courseCommentRepository.save(comment);
    }
}
