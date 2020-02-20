package io.github.batizhao.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Handle 400,500 error
 *
 * @author batizhao
 * @since 2020-02-20
 */
@RestController
@Slf4j
public class ErrorHandler implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @GetMapping("/error")
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseInfo handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        log.error("ErrorHandler statusCode: {}, exception: {}", statusCode, exception);

        // 如果等于 400 错误，则抛出设定的枚举类中的错误信息
        if (HttpStatus.NOT_FOUND.value() == statusCode) {
            return new ResponseInfo().setMessage(ResultEnum.NOT_FOUNT_RESOURCE.getMessage())
                    .setCode(ResultEnum.NOT_FOUNT_RESOURCE.getCode());
        }

        // 返回默认错误
        return new ResponseInfo().setMessage(ResultEnum.UNKNOWN_ERROR.getMessage())
                .setCode(ResultEnum.UNKNOWN_ERROR.getCode());
    }

}
