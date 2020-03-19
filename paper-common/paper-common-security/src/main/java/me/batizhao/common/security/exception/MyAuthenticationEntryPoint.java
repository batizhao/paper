package me.batizhao.common.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.common.core.util.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义 401 返回数据格式
 *
 * 如果异常是 AuthenticationException，使用 AuthenticationEntryPoint 处理
 * 如果异常是 AccessDeniedException 且用户是匿名用户，使用 AuthenticationEntryPoint 处理
 *
 * 在 OAuth 开启后，如果用户携带一个过期 Token，使用 AuthenticationEntryPoint 处理
 *
 * 1. InvalidTokenException: Access token expired 测试用例：givenExpiredToken_whenGetSecureRequest_thenUnauthorized
 * 2. givenNoToken_whenGetSecureRequest_thenUnauthorized
 * 3. givenInvalidToken_whenGetSecureRequest_thenUnauthorized
 *
 * @author batizhao
 * @since 2020-03-02
 **/
@Component
@Slf4j
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        ResponseInfo<String> message = new ResponseInfo<String>().setCode(ResultEnum.PERMISSION_UNAUTHORIZED_ERROR.getCode())
                .setMessage(ResultEnum.PERMISSION_UNAUTHORIZED_ERROR.getMessage())
                .setData(authException.getMessage());

        if(authException instanceof InsufficientAuthenticationException) {
            message = new ResponseInfo<String>().setCode(ResultEnum.OAUTH2_TOKEN_INVALID.getCode())
                    .setMessage(ResultEnum.OAUTH2_TOKEN_INVALID.getMessage())
                    .setData(authException.getMessage());
        }

        log.error("Authentication Exception Handler for 401.", authException);
        response.getWriter().write(new ObjectMapper().writeValueAsString(message));
    }
}
