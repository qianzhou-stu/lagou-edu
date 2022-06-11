package com.lagou.common.web.exception;

import com.lagou.common.entity.vo.Result;
import com.lagou.common.exception.BaseException;
import com.lagou.common.exception.ServiceException;
import com.lagou.common.exception.SystemErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;

/**
 * @ClassName DefaultGlobalExceptionHandlerAdvice
 * @Description 统一异常处理格式
 * @Author zhouqian
 * @Date 2022/4/8 14:50
 * @Version 1.0
 */
@Slf4j
public class DefaultGlobalExceptionHandlerAdvice {

    @ExceptionHandler(value = {MultipartException.class})
    public Result uploadFileLimitException(MultipartException ex) {
        log.error("upload file size limit:{}", ex.getMessage());
        return Result.fail(SystemErrorType.UPLOAD_FILE_SIZE_LIMIT);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public Result serviceException(MethodArgumentNotValidException ex) {
        log.error("service exception:{}", ex.getMessage());
        return Result.fail(SystemErrorType.ARGUMENT_NOT_VALID, ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(value = {DuplicateKeyException.class})
    public Result duplicationKeyException(DuplicateKeyException ex) {
        log.error("primary key duplication exception:{}", ex.getMessage());
        return Result.fail(SystemErrorType.DUPLICATE_PRIMARY_KEY);
    }

    @ExceptionHandler(value = {BaseException.class})
    public Result baseException(BaseException ex) {
        log.info("base exception:{}", ex.getMessage());
        return Result.fail(ex.getErrorType());
    }

    @ExceptionHandler(value = {ServiceException.class})
    public Result serviceException(ServiceException ex){
        log.info("service exception:{}", ex.getMessage());
        return Result.fail(ex.getErrorType());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result exception() {
        return Result.fail();
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result throwable() {
        return Result.fail();
    }

}
