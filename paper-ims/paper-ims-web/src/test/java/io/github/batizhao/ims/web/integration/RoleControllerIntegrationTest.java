package io.github.batizhao.ims.web.integration;

import io.github.batizhao.common.core.constant.SecurityConstants;
import io.github.batizhao.common.core.util.ResultEnum;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author batizhao
 * @since 2020-03-18
 **/
public class RoleControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Test
    public void givenUserId_whenFindRoles_thenSuccess() throws Exception {
        mvc.perform(get("/role").param("userId", "1")
                .header(SecurityConstants.FROM, SecurityConstants.FROM_IN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    public void givenUserId_whenFindRoles_thenZero() throws Exception {
        mvc.perform(get("/role").param("userId", "3")
                .header(SecurityConstants.FROM, SecurityConstants.FROM_IN))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }
}
