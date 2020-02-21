package io.github.batizhao.exception;

import lombok.extern.slf4j.Slf4j;
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
    public ResponseInfo<String> defaultErrorHandler(Exception e) {
        log.error("Default Exception", e);
        return new ResponseInfo<String>().setCode(ResultEnum.UNKNOWN_ERROR.getCode())
                .setMessage(ResultEnum.UNKNOWN_ERROR.getMessage())
                .setData(e.getMessage());
    }
}
