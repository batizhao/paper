package io.github.batizhao.web.integration;

import io.github.batizhao.PaperApplication;
import io.github.batizhao.exception.ResultEnum;
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
     * TODO: 和 curl、postman 返回信息不匹配
     * curl -X POST --user 'client_app:xxxx' localhost:8080/oauth/token\?grant_type=password\&username=admin\&password=123456
     * 会重定向到 ErrorController ，然后被 WebExceptionHandler#handleHttpRequestMethodNotSupportedException 捕获返回 401 错误
     *
     * 在 MockMvc 这里，不会被重定向到 /error
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

    @Test
    public void givenNoToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        mvc.perform(get("/api/user/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PERMISSION_UNAUTHORIZED_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("Full authentication is required")));
    }

    @Test
    public void givenExpiredToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE1ODMwNDQwNzMsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI1MzUyODEwYi1iNDhjLTQ4ZmQtYTJmZi1jYTM3OGM2OGUyNGMiLCJjbGllbnRfaWQiOiJjbGllbnRfYXBwIiwidXNlcm5hbWUiOiJhZG1pbiJ9.Go8txRMeNhD9Z6ZPBQjaCmIzjoHLv4fb2ACeOB_M1rc";
        mvc.perform(get("/api/user")
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PERMISSION_UNAUTHORIZED_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("Access token expired")));
    }

    @Test
    public void givenInvalidToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        mvc.perform(get("/api/user")
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PERMISSION_UNAUTHORIZED_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("Cannot convert access token")));
    }
}
