package me.batizhao.common.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.common.core.util.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义 403 返回数据格式
 * 指的是登录了但是由于权限不足(比如普通用户访问管理员界面）
 * 如果异常是 AccessDeniedException 且用户不是匿名用户，使用 AccessDeniedHandler 处理
 *
 * 如果定义了全局异常处理器 @ExceptionHandler ，这个异常会提前被拦截
 *
 * @author batizhao
 * @since 2020-03-02
 **/
@Component
@Slf4j
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        ResponseInfo<String> message = new ResponseInfo<String>().setCode(ResultEnum.PERMISSION_FORBIDDEN_ERROR.getCode())
                .setMessage(ResultEnum.PERMISSION_FORBIDDEN_ERROR.getMessage())
                .setData(accessDeniedException.getMessage());

        log.error("Access Denied Handler for 403.", accessDeniedException);
        response.getWriter().write(new ObjectMapper().writeValueAsString(message));
    }
}
