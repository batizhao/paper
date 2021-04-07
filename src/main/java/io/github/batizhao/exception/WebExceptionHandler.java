package io.github.batizhao.exception;

import lombok.extern.slf4j.Slf4j;
import io.github.batizhao.util.ResponseInfo;
import io.github.batizhao.util.ResultEnum;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolationException;
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
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseInfo<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("Http Request Method Not Supported Exception!", e);
        return new ResponseInfo<String>().setCode(ResultEnum.PARAMETER_INVALID.getCode())
                .setMessage(ResultEnum.PARAMETER_INVALID.getMessage())
                .setData(e.getMessage());
    }

    /**
     * RequestBody Validation
     *
     * @param e MethodArgumentNotValidException
     * @return ResponseInfo<List<String>>
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseInfo<List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        //Get all errors
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        log.error("Method Argument Not Valid Exception, errors is {}", errors, e);
        return new ResponseInfo<List<String>>().setCode(ResultEnum.PARAMETER_INVALID.getCode())
                .setMessage(ResultEnum.PARAMETER_INVALID.getMessage())
                .setData(errors);
    }

    /**
     * Path Variables Validation
     *
     * @param e ConstraintViolationException
     * @return ResponseInfo<List<String>>
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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

    /**
     * 处理 400 异常
     * @param e
     * @return ResponseInfo<String>
     */
    @ExceptionHandler({BindException.class, TypeMismatchException.class, HttpMessageNotWritableException.class,
            MissingServletRequestPartException.class, HttpMessageNotReadableException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseInfo<String> handleBadRequestException(Exception e) {
        log.error("BadRequestException!", e);
        return new ResponseInfo<String>().setCode(ResultEnum.PARAMETER_INVALID.getCode())
                .setMessage(ResultEnum.PARAMETER_INVALID.getMessage())
                .setData(e.getMessage());
    }

    /**
     * 找不到记录异常。
     * 和 404 共用一个返回消息，但返回状态码是 200。
     * @param e NotFoundException
     * @return ResponseInfo<String>
     */
    @ExceptionHandler
    public ResponseInfo<String> handleNotFoundException(NotFoundException e) {
        log.error("NotFoundException!", e);
        return new ResponseInfo<String>().setCode(ResultEnum.RESOURCE_NOT_FOUND.getCode())
                .setMessage(ResultEnum.RESOURCE_NOT_FOUND.getMessage())
                .setData(e.getMessage());
    }

    @ExceptionHandler(StorageException.class)
    @ResponseStatus
    public ResponseInfo<String> handleStorageException(StorageException e) {
        log.error("StorageException!", e);
        return new ResponseInfo<String>().setCode(ResultEnum.SYSTEM_STORAGE_ERROR.getCode())
                .setMessage(ResultEnum.SYSTEM_STORAGE_ERROR.getMessage())
                .setData(e.getMessage());
    }

    @ExceptionHandler(DataSourceException.class)
    @ResponseStatus
    public ResponseInfo<String> handleDataSourceException(DataSourceException e) {
        log.error("DataSourceException!", e);
        return new ResponseInfo<String>().setCode(ResultEnum.DP_DS_ERROR.getCode())
                .setMessage(ResultEnum.DP_DS_ERROR.getMessage())
                .setData(e.getMessage());
    }

    /**
     * 默认异常处理
     * 特殊处理，这里会优先捕获 AccessDeniedException，造成 accessDeniedHandler 无效，造成返回错误消息
     *
     * @param e Exception
     * @return ResponseInfo<String>
     */
    @ExceptionHandler
    @ResponseStatus
    public ResponseInfo<String> handleDefault(Exception e) {
        if (e instanceof AccessDeniedException) {
            throw new AccessDeniedException(e.getMessage());
        }
        log.error("Default Exception!", e);
        return ResponseInfo.failed(e.getMessage());
    }
}
