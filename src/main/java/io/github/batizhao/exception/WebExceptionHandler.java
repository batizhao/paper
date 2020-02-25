package io.github.batizhao.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.security.AccessControlException;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * RequestBody Validation
     * @param e MethodArgumentNotValidException
     * @return
     */
    @ExceptionHandler
    public ResponseInfo<List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        //Get all errors
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        log.error("MethodArgumentNotValidException, errors is {}", errors, e);
        return new ResponseInfo<List<String>>().setCode(ResultEnum.PARAMETER_INVALID.getCode())
                .setMessage(ResultEnum.PARAMETER_INVALID.getMessage())
                .setData(errors);
    }

    /**
     * Path Variables Validation
     * @param e ConstraintViolationException
     * @return
     */
    @ExceptionHandler
    public ResponseInfo<List<String>> handleConstraintViolationException(ConstraintViolationException e) {
        //Get all errors
        List<String> errors = e.getConstraintViolations()
                .stream()
                .map(x -> x.getMessage())
                .collect(Collectors.toList());

        log.error("ConstraintViolationException, errors is {}", errors, e);
        return new ResponseInfo<List<String>>().setCode(ResultEnum.PARAMETER_INVALID.getCode())
                .setMessage(ResultEnum.PARAMETER_INVALID.getMessage())
                .setData(errors);
    }

    @ExceptionHandler
    public ResponseInfo<String> defaultErrorHandler(Exception e) {
        log.error("Default Exception", e);
        return new ResponseInfo<String>().setCode(ResultEnum.UNKNOWN_ERROR.getCode())
                .setMessage(ResultEnum.UNKNOWN_ERROR.getMessage())
                .setData(e.getMessage());
    }
}
