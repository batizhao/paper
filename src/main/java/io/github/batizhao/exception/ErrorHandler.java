package io.github.batizhao.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Handle 404 error
 * <p>
 * 由于在 SpringBoot 中已经对 404 这类错误进行了处理，这里我们想要对这些错误进行自己的处理就必须按下方式来进行改造，
 * 继承 ErrorController 类，重写其中的 getErrorPath() 方法，指定发送这类错误时跳转的路径。这里对这个路径进行重写，
 * 然后再写一个 Controller 处理器，返回错误信息。
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
    @ResponseStatus(HttpStatus.OK)
    public ResponseInfo<String> handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        String exception_type = (String) request.getAttribute("javax.servlet.error.exception_type");

        log.error("ErrorHandler statusCode: {}, requestUri: {}, exception_type: {}",
                statusCode, requestUri, exception_type);

        return new ResponseInfo<String>().setMessage(ResultEnum.RESOURCE_NOT_FOUND.getMessage())
                .setCode(ResultEnum.RESOURCE_NOT_FOUND.getCode())
                .setData(requestUri);
    }
}
