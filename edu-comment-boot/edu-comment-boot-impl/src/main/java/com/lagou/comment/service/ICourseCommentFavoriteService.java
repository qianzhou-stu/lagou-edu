package com.lagou.comment.service;

import com.lagou.comment.entity.CourseCommentFavorite;
import com.lagou.edu.comment.api.dto.CourseCommentFavoriteDTO;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ICourseCommentFavoriteService {
    List<CourseCommentFavorite> getCommentFavoriteRecordList(Integer userId, List<String> parentIds);

    CourseCommentFavoriteDTO favorite(Integer userId, String commentId);

    boolean cancelFavorite(Integer userId, String commentId);
}
