package com.lagou.course.entity;

import java.util.Date;
import lombok.Data;

@Data
public class CourseSection {
    /**
    * id
    */
    private Integer id;

    /**
    * 课程id
    */
    private Integer courseId;

    /**
    * 章节名
    */
    private String sectionName;

    /**
    * 章节描述
    */
    private String description;

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
    private Boolean isDe;

    /**
    * 排序字段
    */
    private Integer orderNum;

    /**
    * 状态，0:隐藏；1：待更新；2：已发布
    */
    private Integer status;
}