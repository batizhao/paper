package me.batizhao.uaa.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * 因为 ExceptionHandler 不能捕获到 OAuth 中的异常，所以如果要统一返回消息，
 * 需要重写 DefaultWebResponseExceptionTranslator
 * 这里主要是和访问 /oauth/token 相关的异常
 *
 * 1. InvalidGrantException: Invalid refresh token givenInvalidRefreshToken_whenGetAccessToken_thenOAuthException
 * 2. InvalidTokenException: Invalid refresh token (expired) givenInvalidRefreshToken_whenGetAccessToken_thenOAuthException
 * 3. InvalidGrantException: 用户名或密码错误 givenNoPassword_whenGetAccessToken_thenOAuthException
 * 4. InvalidRequestException: Missing grant type givenNoGrantType_whenGetAccessToken_thenOAuthException
 *
 * @author batizhao
 * @since 2020-03-02
 **/
@Component
@Slf4j
public class MyWebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);
        OAuth2Exception body = responseEntity.getBody();
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(responseEntity.getHeaders().toSingleValueMap());

        log.error("OAuth2 Exception!", e);
        return ResponseEntity.status(HttpStatus.OK).body(new MyOAuth2Exception(body.getMessage()));
    }
}
