package io.github.batizhao.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author batizhao
 * @since 2020-02-20
 */
@RestControllerAdvice
@Slf4j
public class WebExceptionHandler {

    @ExceptionHandler
    public ResponseInfo MethodNotSupportedHandler(HttpRequestMethodNotSupportedException e) {
        log.error("Method Not Supported", e);
        return new ResponseInfo().setCode(ResultEnum.NOT_FOUNT_RESOURCE.getCode())
                .setMessage(ResultEnum.NOT_FOUNT_RESOURCE.getMessage());
    }

    @ExceptionHandler
    public ResponseInfo globalExceptionHandler(Exception e) {
        log.error("Global Exception", e);
        return new ResponseInfo().setCode(ResultEnum.UNKNOWN_ERROR.getCode())
                .setMessage(ResultEnum.UNKNOWN_ERROR.getMessage());
    }
}
