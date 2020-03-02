package io.github.batizhao.web.integration;

import io.github.batizhao.exception.ResultEnum;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * OAuth 安全框架集成测试
 * 这些测试只有 @EnableAuthorizationServer 的情况下有意义
 *
 * @author batizhao
 * @since 2020-03-02
 **/
public class OAuthIntegrationTest extends BaseControllerIntegrationTest {

    @Test
    public void givenNoToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        mvc.perform(get("/api/user/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PERMISSION_UNAUTHORIZED_ERROR.getCode()));
    }

    @Test
    public void givenExpiredToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE1ODMwNDQwNzMsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiI1MzUyODEwYi1iNDhjLTQ4ZmQtYTJmZi1jYTM3OGM2OGUyNGMiLCJjbGllbnRfaWQiOiJjbGllbnRfYXBwIiwidXNlcm5hbWUiOiJhZG1pbiJ9.Go8txRMeNhD9Z6ZPBQjaCmIzjoHLv4fb2ACeOB_M1rc";
        mvc.perform(get("/api/user")
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PERMISSION_UNAUTHORIZED_ERROR.getCode()));
    }

    @Test
    public void givenInvalidToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        mvc.perform(get("/api/user")
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PERMISSION_UNAUTHORIZED_ERROR.getCode()));
    }
}
