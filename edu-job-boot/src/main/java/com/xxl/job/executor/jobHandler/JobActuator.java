package com.xxl.job.executor.jobHandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;

@Component
public class JobActuator {

    public ReturnT<String> actuate(String name, JobExecutor executor){
        try {
            XxlJobLogger.log(name + "-任务开始.");
            executor.execute();
            XxlJobLogger.log(name + "-任务结束.");
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            XxlJobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}
