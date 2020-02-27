package io.github.batizhao.web.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.batizhao.domain.User;
import io.github.batizhao.exception.ResultEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @since 2020-02-11
 */
public class UserControllerIntegrationTest extends BaseControllerIntegrationTest{

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(roles={"USER"})
    public void whenGetUserByUserName_thenReturnJson() throws Exception {
        mvc.perform(get("/user/username").param("username", "bob"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.email").value("bob@qq.com"));
    }

    /**
     * Param 或者 PathVariable 校验失败的情况
     * @throws Exception
     */
    @Test
    @WithMockUser(roles={"USER"})
    public void whenGetUser_thenValidateFailed() throws Exception {
        mvc.perform(get("/user/username").param("username", "xx"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()));
    }

    @Test
    public void whenGetUsers_thenReturnJsonArray() throws Exception {
        mvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(6)))
                .andExpect(jsonPath("$.data[0].username", equalTo("admin")));
    }

    /**
     * 如果需要回滚数据，这里需要加上 @Transactional 注解
     * @throws Exception
     */
    @Test
    @Transactional
    @WithMockUser(roles={"ADMIN"})
    public void whenSaveUser_thenReturnJson() throws Exception {
        User requestBody = new User()
                .setName("daxia").setEmail("daxia@gmail.com").setUsername("daxia")
                .setPassword("123456");

        mvc.perform(post("/user")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));
    }

    /**
     * RequestBody 校验失败的情况
     * @throws Exception
     */
    @Test
    @WithMockUser(roles={"ADMIN"})
    public void whenSaveUser_thenValidateFailed() throws Exception {
        User requestBody = new User().setName("daxia").setEmail("daxia@gmail.com");

        mvc.perform(post("/user")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()));
    }

    @Test
    @Transactional
    @WithMockUser(roles={"ADMIN"})
    public void whenUpdateUser_thenReturnJson() throws Exception {
        User requestBody = new User()
                .setId(8L).setName("daxia").setEmail("daxia@gmail.com").setUsername("daxia")
                .setPassword("123456");

        mvc.perform(post("/user")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @Transactional
    @WithMockUser(roles={"ADMIN"})
    public void whenDeleteUser_thenSuccess() throws Exception {
        mvc.perform(delete("/user/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
    }

    /**
     * 测试参数检验失败的情况
     * @throws Exception
     */
    @Test
    @WithMockUser(roles={"ADMIN"})
    public void whenDeleteUser_thenReturnFail() throws Exception {
        mvc.perform(delete("/user/{id}", -1000L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()));
    }

    @Test
    public void whenGetUsersByName_thenReturnJsonArray() throws Exception {
        mvc.perform(get("/user/name").param("name", "孙波波"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].username", equalTo("bob")))
                .andExpect(jsonPath("$.data[1].email", equalTo("bob2@qq.com")));
    }
}
