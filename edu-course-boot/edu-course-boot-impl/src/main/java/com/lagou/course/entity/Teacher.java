package com.lagou.course.entity;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

@Data
public class Teacher {
    /**
    * id
    */
    private Integer id;

    /**
    * 课程ID
    */
    private Integer courseId;

    /**
    * 讲师姓名
    */
    private String teacherName;

    /**
    * 职务
    */
    private String position;

    /**
    * 讲师介绍
    */
    private String description;

    /**
    * 记录创建时间
    */
    private LocalDateTime createTime;

    /**
    * 更新时间
    */
    private LocalDateTime updateTime;

    /**
    * 是否删除
    */
    private Boolean isDel;
}