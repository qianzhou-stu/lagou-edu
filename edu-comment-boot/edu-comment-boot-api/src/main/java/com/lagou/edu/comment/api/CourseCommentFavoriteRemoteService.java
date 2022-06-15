package com.lagou.edu.comment.api;

import com.lagou.edu.comment.api.dto.CourseCommentFavoriteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "edu-comment-boot-courseCommentFavoriteRemoteService", path = "/commentfavorite")
public interface CourseCommentFavoriteRemoteService {

    @GetMapping("/getUserById")
    List<CourseCommentFavoriteDTO> getUserById(@RequestParam("userId") Integer userId);

    @GetMapping("/favorite")
    CourseCommentFavoriteDTO favorite(@RequestParam("userId") Integer userId,
                                      @RequestParam("commentId") String commentId);

    @GetMapping("/cancelFavorite")
    boolean cancelFavorite(@RequestParam("userId") Integer userId,
                           @RequestParam("commentId") String commentId);
}
