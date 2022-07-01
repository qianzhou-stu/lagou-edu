package com.xxl.job.executor.jobHandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.executor.feign.SyncCourseRemoteServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EduJobHandler extends JobActuator{
    private static Logger logger = LoggerFactory.getLogger(EduJobHandler.class);

    @Autowired
    private SyncCourseRemoteServiceFeign syncCourseRemoteServiceFeign;

    /**
     * 定时更新 课程上下架
     */
    @XxlJob("courseAutoOnlineHandler")
    public ReturnT<String> courseAutoOnlineHandler(String param) throws Exception{
        return actuate("同步课程未上线到上线", ()->syncCourseRemoteServiceFeign.courseAutoOnline());
    }
}
