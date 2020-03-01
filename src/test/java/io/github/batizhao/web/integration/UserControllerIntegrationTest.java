package io.github.batizhao.web.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.batizhao.domain.User;
import io.github.batizhao.exception.ResultEnum;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @since 2020-02-11
 */
public class UserControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Test
    @WithMockUser(roles = {"USER"})
    public void givenUserName_thenFindUser_returnUserJson() throws Exception {
        String accessToken = obtainAccessToken(USERNAME, PASSWORD);

        mvc.perform(get("/user/username").param("username", "bob")
                .header("Authorization", "Bearer " + accessToken))
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
    @WithMockUser(roles = {"USER"})
    public void givenUserName_thenFindUser_returnValidateFailed() throws Exception {
        String accessToken = obtainAccessToken(USERNAME, PASSWORD);
        mvc.perform(get("/user/username").param("username", "xx")
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()));
    }

    @Test
    @WithMockUser
    public void givenName_thenFindUser_returnUserListJson() throws Exception {
        String accessToken = obtainAccessToken(USERNAME, PASSWORD);

        mvc.perform(get("/user/name").param("name", "孙波波")
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].username", equalTo("bob")))
                .andExpect(jsonPath("$.data[1].email", equalTo("bob2@qq.com")));
    }

    @Test
    @WithMockUser
    public void givenId_thenFindUser_ReturnUserJson() throws Exception {
        String accessToken = obtainAccessToken(USERNAME, PASSWORD);
        mvc.perform(get("/user/{id}", 1L)
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.email").value("admin@qq.com"));
    }

    @Test
    @WithMockUser
    public void givenNothing_thenFindAllUser_returnUserListJson() throws Exception {
        String accessToken = obtainAccessToken(USERNAME, PASSWORD);
        mvc.perform(get("/user")
                .header("Authorization", "Bearer " + accessToken))
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
    @WithMockUser(roles = {"ADMIN"})
    public void givenJson_thenSaveUser_returnSucceedJson() throws Exception {
        String accessToken = obtainAccessToken(USERNAME, PASSWORD);
        User requestBody = new User()
                .setName("daxia").setEmail("daxia@gmail.com").setUsername("daxia")
                .setPassword("123456");

        mvc.perform(post("/user")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andExpect(jsonPath("$.data.username", equalTo("daxia")));
    }

    /**
     * RequestBody 校验失败的情况
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void givenJson_thenSaveUser_returnValidateFailed() throws Exception {
        String accessToken = obtainAccessToken(USERNAME, PASSWORD);
        User requestBody = new User().setName("daxia").setEmail("daxia@gmail.com");

        mvc.perform(post("/user")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()));
    }

    @Test
    @Transactional
    @WithMockUser(roles = {"ADMIN"})
    public void givenJson_thenUpdateUser_returnSucceedJson() throws Exception {
        String accessToken = obtainAccessToken(USERNAME, PASSWORD);
        User requestBody = new User()
                .setId(8L).setName("daxia").setEmail("daxia@gmail.com").setUsername("daxia")
                .setPassword("123456");

        mvc.perform(post("/user")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.username", equalTo("daxia")));
    }

    @Test
    @Transactional
    @WithMockUser(roles = {"ADMIN"})
    public void givenId_thenDeleteUser_returnSucceed() throws Exception {
        String accessToken = obtainAccessToken(USERNAME, PASSWORD);
        mvc.perform(delete("/user/{id}", 1L)
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
    }

    /**
     * 测试参数检验失败的情况
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void givenId_thenDeleteUser_returnValidateFail() throws Exception {
        String accessToken = obtainAccessToken(USERNAME, PASSWORD);
        mvc.perform(delete("/user/{id}", -1000L)
                .header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()));
    }
}
