package io.github.batizhao.web.integration;

import io.github.batizhao.PaperApplication;
import io.github.batizhao.exception.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * OAuth 安全框架集成测试
 * 这些测试只有 @EnableAuthorizationServer 的情况下有意义
 * 这个类不需要预先获取 access_token，所以不要继承基类
 *
 * @author batizhao
 * @since 2020-03-02
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PaperApplication.class)
@AutoConfigureMockMvc
//@SpringBootTest(classes = PaperApplication.class)
//@ContextConfiguration
//@WebAppConfiguration
public class OAuthIntegrationTest {

    public static final String USERNAME = "admin";
    public static final String PASSWORD = "123456";
    public static final String CLIENT_ID = "client_app";
    public static final String CLIENT_SECRET = "123456";
    public static final String GRANT_TYPE = "password";

    @Autowired
    MockMvc mvc;

//    @Autowired
//    private WebApplicationContext context;
//
//    private MockMvc mvc;
//
//    @Before
//    public void setUp() {
//        mvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//    }

    /**
     * MockMvc 不是一个真正的 servlet server，所以，并不支持 ErrorController
     * 可以看这个 issues：https://github.com/spring-projects/spring-boot/issues/5574
     *
     * curl -X POST --user 'client_app:xxxx' localhost:8080/oauth/token\?grant_type=password\&username=admin\&password=123456
     * 这个方法失败后会重定向到 ErrorController ，然后被 WebExceptionHandler#handleHttpRequestMethodNotSupportedException 捕获返回 401 错误
     *
     * {
     *   "code": 100001,
     *   "message": "参数不合法！",
     *   "data": "Request method 'POST' not supported"
     * }
     *
     * 在 MockMvc 这里，不会被重定向到 /error，直接就返回 401。所以，并不能给到期望的自定义消息。
     *
     * @throws Exception
     */
    @Test
    public void givenErrorSecret_whenGetAccessToken_thenUnauthorized() throws Exception {
        mvc.perform(post("/oauth/token")
                .param("grant_type", GRANT_TYPE).param("username", USERNAME).param("password", PASSWORD)
                .with(httpBasic(CLIENT_ID, "xxxx")))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenNoGrantType_whenGetAccessToken_thenOAuthException() throws Exception {
        mvc.perform(post("/oauth/token")
                .param("username", USERNAME).param("password", PASSWORD)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("Missing grant type")));
    }

    /**
     * 这里需要设置 spring.mvc.locale: zh_CN，否则会调用 spring security core 中的 messages.properties
     * 返回国际化消息 Bad credentials
     * 在 postman、curl 等方式中自动会返回中文消息
     *
     * @throws Exception
     */
    @Test
    public void givenNoPassword_whenGetAccessToken_thenOAuthException() throws Exception {
        mvc.perform(post("/oauth/token")
                .param("grant_type", GRANT_TYPE).param("username", USERNAME)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("用户名或密码错误")));
    }

    /**
     * 1. 当用一个过期的 refresh token 去请求 accesss token 时，抛出 InvalidTokenException: Invalid refresh token (expired)
     * 2. 当用一个非法的 refresh token 去请求 accesss token 时，抛出 InvalidGrantException: Invalid refresh token
     *
     * 客户端只要处理 100002 状态码，并匹配 Invalid refresh token 消息，重新使用 grant_type=password 进行请求 accesss token
     *
     * @throws Exception
     */
    @Test
    public void givenExpiredRefreshToken_whenGetAccessToken_thenOAuthException() throws Exception {
        String expiredRefreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJhdGkiOiIxODA3MDllYy1kMGJjLTQ0MzItOTNkNy03NTY5OWY1NDc4Y2QiLCJleHAiOjE1ODM0NTgyMTEsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwianRpIjoiYzFiZjgzYjQtNGM0OC00ZWE1LWJlYWMtMWVjMzIwZjBhYWIwIiwiY2xpZW50X2lkIjoiY2xpZW50X2FwcCIsInVzZXJuYW1lIjoiYWRtaW4ifQ.IPhNSl9ZNyajJTiAmzNoHe4u01jnxUBz-Gw2tUl1tiI";
        mvc.perform(post("/oauth/token")
                .param("grant_type", "refresh_token").param("refresh_token", expiredRefreshToken)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("Invalid refresh token (expired)")));
    }

    @Test
    public void givenInvalidRefreshToken_whenGetAccessToken_thenOAuthException() throws Exception {
        String invalidRefreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6";
        mvc.perform(post("/oauth/token")
                .param("grant_type", "refresh_token").param("refresh_token", invalidRefreshToken)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("Cannot convert access token to JSON")));
    }

    //    @Test
//    public void givenRefreshToken_whenGetAccessToken_thenSuccess() throws Exception {
//        String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0b20iLCJzY29wZSI6WyJhbGwiXSwiYXRpIjoiYmY1Zjc0NTQtOWJmNi00MjkyLWFiYmYtODUyNWM4ZDFmZjQwIiwiZXhwIjoxNTg0MTcwNTIyLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSwianRpIjoiMjVkM2RiOTUtYmI4Yy00OWEzLTk2YzgtYjU1NjcwMGU4NjJhIiwiY2xpZW50X2lkIjoiY2xpZW50X2FwcCIsInVzZXJuYW1lIjoidG9tIn0.tidD9hr0E6vx9WFXvAlnHMbjmnD0sXnC-c7TH_tdZt0";
//
//        mvc.perform(post("/oauth/token")
//                .param("grant_type", "refresh_token").param("refresh_token", refreshToken)
//                .with(httpBasic(CLIENT_ID, CLIENT_SECRET)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.token_type", equalTo("bearer")));
//    }

    @Test
    public void givenExpiredToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE1ODMwNDQwNzMsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI1MzUyODEwYi1iNDhjLTQ4ZmQtYTJmZi1jYTM3OGM2OGUyNGMiLCJjbGllbnRfaWQiOiJjbGllbnRfYXBwIiwidXNlcm5hbWUiOiJhZG1pbiJ9.Go8txRMeNhD9Z6ZPBQjaCmIzjoHLv4fb2ACeOB_M1rc";
        mvc.perform(get("/user")
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Access token expired")));
    }

    @Test
    public void givenNoToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        mvc.perform(get("/user/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Full authentication is required")));
    }

    @Test
    public void givenInvalidToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        mvc.perform(get("/user")
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Cannot convert access token")));
    }
}
