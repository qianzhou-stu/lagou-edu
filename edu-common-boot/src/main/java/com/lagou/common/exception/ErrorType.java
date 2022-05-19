package com.lagou.common.exception;

/**
 * @ClassName ErrorType
 * @Description TODO
 * @Author zhouqian
 * @Date 2022/4/8 10:00
 * @Version 1.0
 */

public interface ErrorType {
    /**
     * 返回code
     *
     * @return
     */
    String getCode();
    /**
     * 返回mesg
     * @return
     */
    String getMesg();
}
