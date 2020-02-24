package io.github.batizhao.web;

import io.github.batizhao.exception.ErrorHandler;
import io.github.batizhao.exception.ResultEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 异常测试
 *
 * @author batizhao
 * @since 2020-02-24
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ErrorHandler.class)
public class ErrorHandlerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void test404NotFound() throws Exception {
        mvc.perform(get("/error"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.RESOURCE_NOT_FOUND.getCode()));
    }

}