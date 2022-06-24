package com.xxl.job.executor.jobHandler;

/**
 * Job任务执行接口
 */
public interface JobExecutor {
    /**
     * Job任务执行方法
     * @throws Exception
     */
    public void execute() throws Exception;
}
