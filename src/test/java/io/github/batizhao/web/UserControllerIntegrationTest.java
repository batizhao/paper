package io.github.batizhao.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.batizhao.PaperApplication;
import io.github.batizhao.domain.User;
import io.github.batizhao.exception.ResultEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 在集成测试中，不再需要 Mock Bean 和 Stub，
 * 但是需要实例化整个上下文，再对返回数据进行判断
 * 参数校验相关的测试可以放在集成测试中，因为不需要 Stub Data
 *
 * @author batizhao
 * @since 2020-02-11
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PaperApplication.class)
@AutoConfigureMockMvc
//@TestPropertySource(
//        locations = "classpath:application-integrationtest.properties")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void whenGetUser_thenReturnJson() throws Exception {
        mvc.perform(get("/user/{username}", "bob"))
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
    public void whenGetUser_thenValidateFailed() throws Exception {
        mvc.perform(get("/user/{username}", "xx"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()));
    }

    @Test
    public void whenGetUsers_thenReturnJsonArray() throws Exception {
        mvc.perform(get("/user/index"))
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
    public void whenSaveUser_thenReturnJson() throws Exception {
        User requestBody = User.builder()
                .name("daxia").email("daxia@gmail.com").username("daxia")
                .password("123456").build();

        mvc.perform(post("/user")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.username").value("daxia"))
                .andExpect(jsonPath("$.data.email").value("daxia@gmail.com"));
    }

    /**
     * RequestBody 校验失败的情况
     * @throws Exception
     */
    @Test
    public void whenSaveUser_thenValidateFailed() throws Exception {
        User requestBody = User.builder()
                .name("daxia").email("daxia@gmail.com").build();

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
    public void whenUpdateUser_thenReturnJson() throws Exception {
        User requestBody = User.builder()
                .id(8L).name("daxia").email("daxia@gmail.com").username("daxia")
                .password("123456").build();

        mvc.perform(post("/user")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.username").value("daxia"))
                .andExpect(jsonPath("$.data.email").value("daxia@gmail.com"));
    }

    @Test
    @Transactional
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
