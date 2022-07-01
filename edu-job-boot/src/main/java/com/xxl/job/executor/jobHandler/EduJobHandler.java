package com.xxl.job.executor.jobHandler;

import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.executor.feign.CourseRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.xxl.job.core.log.XxlJobLogger;


@Component
public class EduJobHandler {
    private static Logger logger = LoggerFactory.getLogger(EduJobHandler.class);

    @Autowired
    private CourseRemoteService courseRemoteService;

    /**
     * 定时更新 课程上下架
     */
    @XxlJob("courseAutoOnline")
    public void courseAutoOnline(){
        try{
            XxlJobLogger.log("开始课程上架处理操作");
            courseRemoteService.courseAutoOnline();
            XxlJobLogger.log("课程上架处理操作结束");
        }catch (Exception e){
            XxlJobLogger.log(e);

        }
    }
}
