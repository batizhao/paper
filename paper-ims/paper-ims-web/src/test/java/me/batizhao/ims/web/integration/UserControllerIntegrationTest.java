package me.batizhao.ims.web.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.ims.domain.User;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 在 OAuth 开启的情况下，不再需要 @WithMockUser 来模拟用户
 * @author batizhao
 * @since 2020-02-11
 */
public class UserControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Test
    public void givenUserName_whenFindUser_thenUserJson() throws Exception {
        mvc.perform(get("/user/username").param("username", "bob")
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.email").value("bob@qq.com"));
    }

    /**
     * Param 或者 PathVariable 校验失败的情况
     *
     * @throws Exception
     */
    @Test
    public void givenInvalidUserName_whenFindUser_thenValidateFailed() throws Exception {
        mvc.perform(get("/user/username").param("username", "xx")
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data[0]", containsString("个数必须在")));
    }

    @Test
    public void givenUserName_whenFindUser_thenSuccess() throws Exception {
        mvc.perform(get("/user/userdetail").param("username", "admin")
                .header(SecurityConstants.FROM, SecurityConstants.FROM_IN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.email").value("admin@qq.com"))
                .andExpect(jsonPath("$.data.roleList", hasSize(2)));
    }

    @Test
    public void givenUserName_whenFindUser_theNotFound() throws Exception {
        mvc.perform(get("/user/userdetail").param("username", "xxxx")
                .header(SecurityConstants.FROM, SecurityConstants.FROM_IN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.RESOURCE_NOT_FOUND.getCode()));
    }

    /**
     * 传递错误的 @Inner header
     *
     * @throws Exception
     */
    @Test
    public void givenUserNameButInvalidInnerHeader_whenFindUser_then401() throws Exception {
        mvc.perform(get("/user/userdetail").param("username", "xx")
                .header(SecurityConstants.FROM, "No"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Full authentication is required")));
    }

    /**
     * 没有传递 @Inner header
     *
     * @throws Exception
     */
    @Test
    public void givenUserNameButNoInnerHeader_whenFindUser_thenNull() throws Exception {
        mvc.perform(get("/user/userdetail").param("username", "bob"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.UNKNOWN_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("from header")));
    }

    /**
     * Param 丢失的情况
     *
     * @throws Exception
     */
    @Test
    public void givenNoUserName_whenFindUser_thenValidateFailed() throws Exception {
        mvc.perform(get("/user/username")
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Required String parameter")));
    }

    @Test
    public void givenName_whenFindUser_thenUserListJson() throws Exception {
        mvc.perform(get("/user/name").param("name", "孙波波")
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].username", equalTo("bob")))
                .andExpect(jsonPath("$.data[1].email", equalTo("bob2@qq.com")));
    }

    @Test
    public void givenId_whenFindUser_thenUserJson() throws Exception {
        mvc.perform(get("/user/{id}", 1L)
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.email").value("admin@qq.com"));
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
    public void givenNothing_whenFindAllUser_thenUserListJson() throws Exception {
        mvc.perform(get("/user")
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(6)))
                .andExpect(jsonPath("$.data[0].username", equalTo("admin")));
    }

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

    /**
     * 如果需要回滚数据，这里需要加上 @Transactional 注解
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void givenJson_whenSaveUser_thenSucceedJson() throws Exception {
        User requestBody = new User()
                .setName("daxia").setEmail("daxia@gmail.com").setUsername("daxia")
                .setPassword("123456");

        mvc.perform(post("/user")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andExpect(jsonPath("$.data.username", equalTo("daxia")));
    }

    /**
     * RequestBody 校验失败的情况1
     *
     * @throws Exception
     */
    @Test
    public void givenJson_whenSaveUser_thenValidateFailed() throws Exception {
        User requestBody = new User().setName("daxia").setEmail("daxia@gmail.com");

        mvc.perform(post("/user")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data[0]", containsString("is not blank")));
    }

    /**
     * RequestBody 校验失败的情况2
     *
     * @throws Exception
     */
    @Test
    public void givenNoJson_whenSaveUser_thenValidateFailed() throws Exception {
        mvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Required request body is missing")));
    }

    @Test
    @Transactional
    public void givenJson_whenUpdateUser_thenSucceedJson() throws Exception {
        User requestBody = new User()
                .setId(8L).setName("daxia").setEmail("daxia@gmail.com").setUsername("daxia")
                .setPassword("123456");

        mvc.perform(post("/user")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.username", equalTo("daxia")));
    }

    @Test
    @Transactional
    public void givenId_whenDeleteUser_thenSucceed() throws Exception {
        mvc.perform(delete("/user/{id}", 1L)
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));
    }

    /**
     * 测试参数检验失败的情况
     *
     * @throws Exception
     */
    @Test
    public void givenInValidId_whenDeleteUser_thenValidateFail() throws Exception {
        mvc.perform(delete("/user/{id}", -1000L)
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data[0]", containsString("最小不能小于")));
    }

    /**
     * 测试参数检验失败的情况
     *
     * @throws Exception
     */
    @Test
    public void givenStringId_whenDeleteUser_thenValidateFail() throws Exception {
        mvc.perform(delete("/user/{id}", "xxxx")
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Failed to convert value of type")));
    }

    @Test
    public void givenInvalidRole_whenGetSecureRequest_thenForbidden() throws Exception {
        String tom_access_token = obtainAccessToken("tom", "123456");
        mvc.perform(delete("/user/{id}", 3L)
                .header("Authorization", "Bearer " + tom_access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PERMISSION_FORBIDDEN_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("不允许访问")));
    }

    @Test
    public void givenId_whenPutUser_thenMethodNotSupported() throws Exception {
        mvc.perform(put("/user/{id}", 3L)
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Request method 'PUT' not supported")));
    }

    @Test
    public void givenAuthentication_whenGetCurrentUser_thenMe() throws Exception {
        mvc.perform(get("/user/whoiam")
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(USERNAME));
    }
}
