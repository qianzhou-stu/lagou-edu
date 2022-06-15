package com.lagou.comment.service;

import com.lagou.comment.entity.CourseComment;
import com.lagou.edu.comment.api.dto.CourseCommentDTO;
import org.springframework.data.domain.Page;

public interface ICourseCommentService {
    Page<CourseComment> getCourseCommentList(Integer courseId, int pageNum, int pageSize);

    boolean saveCourseComment(CourseCommentDTO comment);

    boolean updateCommentDelStatusByIdAndUserId(String commentId, Integer userId);

    void updateNumOfLike(String commentId, Boolean flag);
}
