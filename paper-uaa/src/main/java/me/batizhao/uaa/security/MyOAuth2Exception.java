package me.batizhao.uaa.security;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 自定义 OAuth2 Exception 响应消息
 *
 * @author batizhao
 * @since 2020-03-02
 **/
@JsonSerialize(using = MyOAuth2ExceptionSerializer.class)
public class MyOAuth2Exception extends OAuth2Exception {

    /**
     * Create a new MyOAuth2Exception with the specified message.
     *
     * @param msg the detail message
     */
    public MyOAuth2Exception(String msg) {
        super(msg);
    }

//    /**
//     * Create a new MyOAuth2Exception with the specified message
//     * and root cause.
//     *
//     * @param msg   the detail message
//     * @param cause the root cause
//     */
//    public MyOAuth2Exception(@Nullable String msg, @Nullable Throwable cause) {
//        super(msg, cause);
//    }
}
