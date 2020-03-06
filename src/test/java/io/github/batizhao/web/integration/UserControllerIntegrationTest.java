package io.github.batizhao.web.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.batizhao.domain.User;
import io.github.batizhao.exception.ResultEnum;
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
//    @WithMockUser(roles = {"USER"})
    public void givenUserName_whenFindUser_thenUserJson() throws Exception {
        mvc.perform(get("/api/user/username").param("username", "bob")
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
//    @WithMockUser(roles = {"USER"})
    public void givenUserName_whenFindUser_thenValidateFailed() throws Exception {
        mvc.perform(get("/api/user/username").param("username", "xx")
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data[0]", containsString("个数必须在")));
    }

    /**
     * Param 丢失的情况
     *
     * @throws Exception
     */
    @Test
//    @WithMockUser(roles = {"USER"})
    public void givenNoUserName_whenFindUser_thenValidateFailed() throws Exception {
        mvc.perform(get("/api/user/username")
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Required String parameter")));
    }

    @Test
//    @WithMockUser
    public void givenName_whenFindUser_thenUserListJson() throws Exception {
        mvc.perform(get("/api/user/name").param("name", "孙波波")
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
//    @WithMockUser
    public void givenId_whenFindUser_thenUserJson() throws Exception {
        mvc.perform(get("/api/user/{id}", 1L)
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.email").value("admin@qq.com"));
    }

    @Test
//    @WithMockUser
    public void givenNothing_whenFindAllUser_thenUserListJson() throws Exception {
        mvc.perform(get("/api/user")
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(6)))
                .andExpect(jsonPath("$.data[0].username", equalTo("admin")));
    }

    /**
     * 如果需要回滚数据，这里需要加上 @Transactional 注解
     *
     * @throws Exception
     */
    @Test
    @Transactional
//    @WithMockUser(roles = {"ADMIN"})
    public void givenJson_whenSaveUser_thenSucceedJson() throws Exception {
        User requestBody = new User()
                .setName("daxia").setEmail("daxia@gmail.com").setUsername("daxia")
                .setPassword("123456");

        mvc.perform(post("/api/user")
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
//    @WithMockUser(roles = {"ADMIN"})
    public void givenJson_whenSaveUser_thenValidateFailed() throws Exception {
        User requestBody = new User().setName("daxia").setEmail("daxia@gmail.com");

        mvc.perform(post("/api/user")
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
//    @WithMockUser(roles = {"ADMIN"})
    public void givenNoJson_whenSaveUser_thenValidateFailed() throws Exception {
        mvc.perform(post("/api/user")
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
//    @WithMockUser(roles = {"ADMIN"})
    public void givenJson_whenUpdateUser_thenSucceedJson() throws Exception {
        User requestBody = new User()
                .setId(8L).setName("daxia").setEmail("daxia@gmail.com").setUsername("daxia")
                .setPassword("123456");

        mvc.perform(post("/api/user")
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
//    @WithMockUser(roles = {"ADMIN"})
    public void givenId_whenDeleteUser_thenSucceed() throws Exception {
        mvc.perform(delete("/api/user/{id}", 1L)
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
//    @WithMockUser(roles = {"ADMIN"})
    public void givenId_whenDeleteUser_thenValidateFail() throws Exception {
        mvc.perform(delete("/api/user/{id}", -1000L)
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data[0]", containsString("最小不能小于")));
    }

    @Test
    public void givenInvalidRole_whenGetSecureRequest_thenForbidden() throws Exception {
        String access_token = obtainAccessToken("tom", "123456");
        mvc.perform(delete("/api/user/{id}", 3L)
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PERMISSION_FORBIDDEN_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("不允许访问")));
    }

    @Test
    public void givenId_whenPutUser_thenMethodNotSupported() throws Exception {
        mvc.perform(put("/api/user/{id}", 3L)
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Request method 'PUT' not supported")));
    }

    @Test
    public void givenAuthentication_whenGetCurrentUser_thenMe() throws Exception {
        mvc.perform(get("/api/user/whoiam")
                .header("Authorization", "Bearer " + access_token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(USERNAME));
    }
}
