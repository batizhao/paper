package io.github.batizhao.web.integration;

import io.github.batizhao.exception.ResultEnum;
import org.junit.Test;
import org.springframework.http.MediaType;

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
    public void givenNothing_thenError_thenErrorPath() throws Exception {
        mvc.perform(get("/error"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.RESOURCE_NOT_FOUND.getCode()));
    }

    /**
     * curl http://localhost:8080/xxx
     * {
     *   "code" : 100010,
     *   "message" : "没有找到相关资源！",
     *   "data" : "/xxx"
     * }
     *
     * 这个方法和实际 Runner 的情况不一样。期望是返回上边的 JSON，因为已经在 ErrorHandler 中做了自定义。
     * 但是因为 MockMvc 不能模拟这种情况，所以这里直接返回 404。
     *
     * @throws Exception
     */
    @Test
    public void givenNothing_thenXxx_thenNotFound() throws Exception {
        mvc.perform(get("/xxx"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}