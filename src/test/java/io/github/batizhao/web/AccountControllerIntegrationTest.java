package io.github.batizhao.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.batizhao.PaperApplication;
import io.github.batizhao.domain.Account;
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
 * 在集成测试中，不再需要 Mock Bean，对数据进行 Stub，
 * 但是需要实例化整个上下文，再对返回数据进行判断
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
public class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void whenGetAccount_thenReturnJson() throws Exception {
        mvc.perform(get("/account/{username}", "zhangsan"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.email").value("zhangsan@qq.com"));
    }

    @Test
    public void whenGetAccounts_thenReturnJsonArray() throws Exception {
        mvc.perform(get("/account/index"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(8)))
                .andExpect(jsonPath("$.data[0].username", equalTo("admin")));
    }

    /**
     * 如果需要回滚数据，这里需要加上 @Transactional 注解
     * @throws Exception
     */
    @Test
    @Transactional
    public void whenSaveAccount_thenReturnJson() throws Exception {
        Account requestBody = Account.builder()
                .name("daxia").email("daxia@gmail.com").username("daxia")
                .password("123456").roles("student").build();

        mvc.perform(post("/account")
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
     * 校验失败的情况
     * @throws Exception
     */
    @Test
    public void whenSaveAccount_thenValidateFailed() throws Exception {
        Account requestBody = Account.builder()
                .name("daxia").email("daxia@gmail.com").build();

        mvc.perform(post("/account")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()));
    }

    @Test
    @Transactional
    public void whenUpdateAccount_thenReturnJson() throws Exception {
        Account requestBody = Account.builder()
                .id(8L).name("daxia").email("daxia@gmail.com").username("daxia")
                .password("123456").roles("student").build();

        mvc.perform(post("/account")
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
    public void whenDeleteAccount_thenSuccess() throws Exception {
        mvc.perform(delete("/account/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
    }

    @Test
    public void whenDeleteAccount_thenReturnFail() throws Exception {
        mvc.perform(delete("/account/{id}", -1000L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.UNKNOWN_ERROR.getCode()));
    }

    @Test
    public void whenGetAccountsByRoles_thenReturnJsonArray() throws Exception {
        mvc.perform(get("/account/role").param("role", "teacher"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].username", equalTo("zhangsan")))
                .andExpect(jsonPath("$.data[1].email", equalTo("lisi@qq.com")));
    }
}
