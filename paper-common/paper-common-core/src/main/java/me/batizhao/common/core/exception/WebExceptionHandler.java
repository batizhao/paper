package me.batizhao.common.core.exception;

import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.common.core.util.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author batizhao
 * @since 2020-02-20
 */
@RestControllerAdvice
@Slf4j
@ResponseStatus(HttpStatus.OK)
public class WebExceptionHandler {

    /**
     * Request Body
     * @param e HttpMessageNotReadableException
     * @return ResponseInfo<String>
     */
    @ExceptionHandler
    public ResponseInfo<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("Http Message Not Readable Exception!", e);
        return new ResponseInfo<String>().setCode(ResultEnum.PARAMETER_INVALID.getCode())
                .setMessage(ResultEnum.PARAMETER_INVALID.getMessage())
                .setData(e.getMessage());
    }

    @ExceptionHandler
    public ResponseInfo<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("Http Request Method Not Supported Exception!", e);
        return new ResponseInfo<String>().setCode(ResultEnum.PARAMETER_INVALID.getCode())
                .setMessage(ResultEnum.PARAMETER_INVALID.getMessage())
                .setData(e.getMessage());
    }

    /**
     * RequestParam Exception
     *
     * @param e MissingServletRequestParameterException
     * @return ResponseInfo<String>
     */
    @ExceptionHandler
    public ResponseInfo<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("Missing Servlet Request Parameter Exception!", e);
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
    public ResponseInfo<String> handleTypeMismatchException(TypeMismatchException e) {
        log.error("TypeMismatchException!", e);
        return new ResponseInfo<String>().setCode(ResultEnum.PARAMETER_INVALID.getCode())
                .setMessage(ResultEnum.PARAMETER_INVALID.getMessage())
                .setData(e.getMessage());
    }

    @ExceptionHandler
    public ResponseInfo<String> handleNotFoundException(NotFoundException e) {
        log.error("NotFoundException!", e);
        return new ResponseInfo<String>().setCode(ResultEnum.RESOURCE_NOT_FOUND.getCode())
                .setMessage(ResultEnum.RESOURCE_NOT_FOUND.getMessage())
                .setData(e.getMessage());
    }

    /**
     * 默认异常处理
     * 这里注意没有捕获全部异常
     * 因为 AccessDeniedException，这里会优先捕获，使自定义的 accessDeniedHandler 无效，造成返回错误消息
     *
     * @param e Exception
     * @return ResponseInfo<String>
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseInfo<String> handleDefault(Exception e) {
        log.error("Default Exception!", e);
        return ResponseInfo.failed(e.getMessage());
    }
}
