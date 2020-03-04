package io.github.batizhao.web.integration;

import io.github.batizhao.exception.ResultEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
public class ErrorHandlerIntegrationTest extends BaseControllerIntegrationTest {

    @Test
    public void whenErrorPath_thenSuccess() throws Exception {
        mvc.perform(get("/error"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.RESOURCE_NOT_FOUND.getCode()));
    }
}