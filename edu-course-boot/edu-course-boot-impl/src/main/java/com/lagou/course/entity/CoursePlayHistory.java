package com.lagou.course.entity;

import java.util.Date;
import lombok.Data;

@Data
public class CoursePlayHistory {
    /**
    * id
    */
    private Integer id;

    /**
    * 用户id
    */
    private Integer userId;

    /**
    * 课程id
    */
    private Integer courseId;

    /**
    * 章节id
    */
    private Integer sectionId;

    /**
    * 课时id
    */
    private Integer lessonId;

    /**
    * 历史播放节点(s)
    */
    private Integer historyNode;

    /**
    * 最高历史播放节点
    */
    private Integer historyHighestNode;

    /**
    * 记录创建时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;

    /**
    * 是否删除
    */
    private Boolean isDel;
}