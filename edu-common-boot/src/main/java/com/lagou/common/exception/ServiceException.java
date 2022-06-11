package com.lagou.common.exception;

/**
 * @ClassName ServiceException
 * @Description ServiceException
 * @Author zhouqian
 * @Date 2022/4/8 10:00
 * @Version 1.0
 */

public class ServiceException extends RuntimeException{


    private ErrorType errorType;

    public ServiceException(ErrorType errorType){
        this.errorType =  errorType;
    }

    public ServiceException(ErrorType errorType, String message){
        super(message);
        this.errorType = errorType;
    }

    public ServiceException(ErrorType errorType, String message, Throwable cause){
        super(message,cause);
        this.errorType = errorType;
    }


    // TODO 对业务异常的返回码进行校验，规范到一定范围内
    private int code = 400;
    private String error;

    public ServiceException(int code, String message){
        super(message);
        this.code = code;
    }

    public ServiceException(int code, String message, String error){
        super(message);
        this.code = code;
        this.error = error;
    }

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }


    public ServiceException(Integer code, String error){
        this.code = code;
        this.error = error;
    }

    public Integer getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
