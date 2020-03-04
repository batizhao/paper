package io.github.batizhao.web.integration;

import io.github.batizhao.PaperApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 在集成测试中，不再需要 Mock Bean 和 Stub，
 * 但是需要实例化整个上下文，再对返回数据进行判断
 * 参数校验相关的测试可以放在集成测试中，因为不需要 Stub Data
 *
 * @author batizhao
 * @since 2020-02-07
 */

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PaperApplication.class)
@AutoConfigureMockMvc
//@WebAppConfiguration
//@TestPropertySource(
//        locations = "classpath:application-integrationtest.properties")
public abstract class BaseControllerIntegrationTest {

    public static final String USERNAME = "admin";
    public static final String PASSWORD = "123456";
    public static final String CLIENT_ID = "client_app";
    public static final String CLIENT_SECRET = "123456";
    public static final String GRANT_TYPE = "password";
    String access_token;

    @Autowired
    MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        access_token = obtainAccessToken(USERNAME, PASSWORD);
    }

    String obtainAccessToken(String username, String password) throws Exception {
        ResultActions result = mvc.perform(post("/oauth/token")
                    .param("grant_type", GRANT_TYPE).param("username", username).param("password", password)
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(httpBasic(CLIENT_ID, CLIENT_SECRET)))
//                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
}
