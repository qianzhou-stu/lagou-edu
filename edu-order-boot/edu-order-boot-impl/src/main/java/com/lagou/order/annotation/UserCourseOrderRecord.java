package com.lagou.order.annotation;

import com.lagou.order.api.enums.StatusTypeEnum;

import java.lang.annotation.*;

@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserCourseOrderRecord {
    StatusTypeEnum type();
}
