package com.lagou.course.entity;

import java.util.Date;
import lombok.Data;

/**
* 课程节内容
*/
@Data
public class CourseLesson {
    /**
    * id
    */
    private Integer id;

    /**
    * 课程id
    */
    private Integer courseId;

    /**
    * 章节id
    */
    private Integer sectionId;

    /**
    * 课时主题
    */
    private String theme;

    /**
    * 课时时长(分钟)
    */
    private Integer duration;

    /**
    * 是否免费
    */
    private Boolean isFree;

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

    /**
    * 排序字段
    */
    private Integer orderNum;

    /**
    * 课时状态,0-隐藏，1-未发布，2-已发布
    */
    private Integer status;
}