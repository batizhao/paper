package io.github.batizhao.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.AccessControlException;

/**
 * @author batizhao
 * @since 2020-02-20
 */
@RestControllerAdvice
@Slf4j
public class WebExceptionHandler {

    @ExceptionHandler
    public ResponseInfo<String> accessControlExceptionHandler(AccessControlException e) {
        log.error("AccessControlException", e);
        return new ResponseInfo<String>().setCode(ResultEnum.PERMISSION_ERROR.getCode())
                .setMessage(ResultEnum.PERMISSION_ERROR.getMessage())
                .setData(e.getMessage());
    }

    @ExceptionHandler(value = {
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class})
    public ResponseInfo<String> handleValidationExceptions(Exception e) {
        log.error("Valid Exception", e);
        return new ResponseInfo<String>().setCode(ResultEnum.PARAMETER_INVALID.getCode())
                .setMessage(ResultEnum.PARAMETER_INVALID.getMessage())
                .setData(e.getMessage());
    }

    @ExceptionHandler
    public ResponseInfo<String> defaultErrorHandler(Exception e) {
        log.error("Default Exception", e);
        return new ResponseInfo<String>().setCode(ResultEnum.UNKNOWN_ERROR.getCode())
                .setMessage(ResultEnum.UNKNOWN_ERROR.getMessage())
                .setData(e.getMessage());
    }
}
