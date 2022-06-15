package com.lagou.comment.remote;

import com.lagou.comment.service.ICourseCommentFavoriteService;
import com.lagou.edu.comment.api.CourseCommentFavoriteRemoteService;
import com.lagou.edu.comment.api.dto.CourseCommentFavoriteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/commentfavorite")
public class CourseCommentFavoriteService implements CourseCommentFavoriteRemoteService {

    @Autowired
    private ICourseCommentFavoriteService courseCommentFavoriteService;

    @Override
    @GetMapping("/getUserById")
    public List<CourseCommentFavoriteDTO> getUserById(@RequestParam("userId") Integer userId) {
        return null;
    }

    @Override
    @GetMapping("/favorite")
    public CourseCommentFavoriteDTO favorite(@RequestParam("userId")Integer userId,
                                             @RequestParam("commentId") String commentId) {
        return courseCommentFavoriteService.favorite(userId, commentId);
    }

    @Override
    @GetMapping("/cancelFavorite")
    public boolean cancelFavorite(@RequestParam("userId") Integer userId,
                                  @RequestParam("commentId") String commentId) {
        return courseCommentFavoriteService.cancelFavorite(userId, commentId);
    }
}
