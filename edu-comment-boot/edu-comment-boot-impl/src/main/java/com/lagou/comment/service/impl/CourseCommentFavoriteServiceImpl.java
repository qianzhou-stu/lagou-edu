package com.lagou.comment.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lagou.comment.entity.CourseCommentFavorite;
import com.lagou.comment.repository.CourseCommentFavoriteRepository;
import com.lagou.comment.service.ICourseCommentFavoriteService;
import com.lagou.comment.service.ICourseCommentService;
import com.lagou.common.date.DateUtil;
import com.lagou.edu.comment.api.dto.CourseCommentFavoriteDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CourseCommentFavoriteServiceImpl implements ICourseCommentFavoriteService {
    @Autowired
    private CourseCommentFavoriteRepository courseCommentFavoriteRepository;
    @Autowired
    private ICourseCommentService courseCommentService;

    @Override
    public List<CourseCommentFavorite> getCommentFavoriteRecordList(Integer userId, List<String> commentIds) {
        if (userId == null || CollectionUtils.isEmpty(commentIds)) {
            return null;
        }
        List<CourseCommentFavorite> favoriteRecords = courseCommentFavoriteRepository.getCommentFavoriteList(userId,commentIds);
        return favoriteRecords;
    }

    @Override
    public CourseCommentFavoriteDTO favorite(Integer userId, String commentId) {
        CourseCommentFavorite commentFavorite = new CourseCommentFavorite();
        commentFavorite.setUserId(userId);
        commentFavorite.setCommentId(commentId);
        commentFavorite.setIsDel(false);
        Example<CourseCommentFavorite> example = Example.of(commentFavorite, ExampleMatcher.matching());
        List<CourseCommentFavorite> courseCommentFavorites = courseCommentFavoriteRepository.findAll(example);
        if (CollectionUtils.isNotEmpty(courseCommentFavorites)){
            CourseCommentFavoriteDTO courseCommentFavoriteDTO = new CourseCommentFavoriteDTO();
            BeanUtils.copyProperties(courseCommentFavorites.get(0), courseCommentFavoriteDTO);
            return courseCommentFavoriteDTO;
        }
        CourseCommentFavorite favorite = new CourseCommentFavorite();
        favorite.setIsDel(false);
        favorite.setUserId(userId);
        favorite.setCommentId(commentId);
        Date nowDate = DateUtil.getNowDate();
        favorite.setCreateTime(nowDate);
        favorite.setUpdateTime(nowDate);
        CourseCommentFavorite insert = courseCommentFavoriteRepository.insert(favorite);
        CourseCommentFavoriteDTO dto = new CourseCommentFavoriteDTO();
        BeanUtils.copyProperties(insert, dto);
        // 点赞成功，更新点赞数量
        courseCommentService.updateNumOfLike(commentId, Boolean.TRUE);
        log.info("用户点赞成功：{}", dto);
        return dto;
    }

    @Override
    public boolean cancelFavorite(Integer userId, String commentId) {
        CourseCommentFavorite commentFavorite = new CourseCommentFavorite();
        commentFavorite.setUserId(userId);
        commentFavorite.setCommentId(commentId);
        commentFavorite.setIsDel(false);

        List<CourseCommentFavorite> courseCommentFavorites = courseCommentFavoriteRepository.findAll(Example.of(commentFavorite, ExampleMatcher.matching()));
        // 如果不为null，存在的时候取消点赞
        if (CollectionUtils.isNotEmpty(courseCommentFavorites)){
            courseCommentFavorites.forEach((courseCommentFavorite)->{
                courseCommentFavorite.setIsDel(true);
                courseCommentFavorite.setUpdateTime(DateUtil.getNowDate());
                courseCommentFavoriteRepository.save(courseCommentFavorite);
                log.info("用户点赞取消：{}", courseCommentFavorite);
            });
        }
        courseCommentService.updateNumOfLike(commentId, Boolean.FALSE);
        return true;
    }
}
