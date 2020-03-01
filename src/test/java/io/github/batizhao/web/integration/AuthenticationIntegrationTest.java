package io.github.batizhao.web.integration;

import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Authentication Test
 *
 * @author batizhao
 * @since 2020-03-01
 **/
public class AuthenticationIntegrationTest extends BaseControllerIntegrationTest {

    @Test
    public void givenNoToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        mvc.perform(get("/user/1"))
                .andExpect(status().isUnauthorized());
    }

//    @Test
//    public void givenInvalidRole_whenGetSecureRequest_thenForbidden() throws Exception {
//        String accessToken = obtainAccessToken("admin", "pass");
//        mvc.perform(get("/user/1")
//                .header("Authorization", "Bearer " + accessToken))
//                .andExpect(status().is4xxClientError());
//    }
}
