package com.xxl.job.executor.feign;

import com.lagou.common.entity.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${remote.feign.edu-course-boot.name:edu-course-boot}", path = "/course")
public interface CourseRemoteService {
    /**
     * 根据配置的自动上架时间，定时任务扫描达到上架时间的草稿状态的课程进行上架。
     */
    @PostMapping(value = "/courseAutoOnline")
    void courseAutoOnline();
}
