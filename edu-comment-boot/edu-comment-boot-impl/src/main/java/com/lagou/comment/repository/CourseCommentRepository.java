package com.lagou.comment.repository;

import com.lagou.comment.entity.CourseComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

@Component
public interface CourseCommentRepository extends MongoRepository<CourseComment, Long> {

}
