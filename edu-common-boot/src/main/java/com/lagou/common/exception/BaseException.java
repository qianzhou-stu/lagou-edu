package com.lagou.common.exception;

import lombok.Getter;

/**
 * @ClassName BaseException
 * @Description BaseException
 * @Author zhouqian
 * @Date 2022/4/8 10:00
 * @Version 1.0
 */

@Getter
public class BaseException extends RuntimeException{
    /**
     * 异常对应的错误类型
     */
    private final ErrorType errorType;
    /**
     * 默认是系统异常
     */
    public BaseException() {
        this.errorType = SystemErrorType.SYSTEM_ERROR;
    }

    public BaseException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

}
