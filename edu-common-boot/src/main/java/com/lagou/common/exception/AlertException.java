package com.lagou.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName AlertException
 * @Description AlertException
 * @Author zhouqian
 * @Date 2022/4/8 09:59
 * @Version 1.0
 */
@Getter
@Setter
public class AlertException extends RuntimeException{
    private static final long serialVersionUID = -4743819198578737692L;

    private int code = 400;
    private String error;

    public AlertException(int code, String message){
        super(message);
        this.code = code;
    }

    public AlertException(int code, String message, String error){
        super(message);
        this.code = code;
        this.error = error;
    }

    public AlertException() {
        super();
    }

    public AlertException(String message) {
        super(message);
    }

    public AlertException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlertException(Throwable cause) {
        super(cause);
    }

}
