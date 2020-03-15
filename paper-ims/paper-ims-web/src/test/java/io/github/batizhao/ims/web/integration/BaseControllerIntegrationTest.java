package io.github.batizhao.ims.web.integration;

import io.github.batizhao.ims.PaperImsApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

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
        classes = PaperImsApplication.class)
@AutoConfigureMockMvc
public abstract class BaseControllerIntegrationTest {

    public static final String USERNAME = "admin";
    public static final String PASSWORD = "123456";
    public static final String CLIENT_ID = "client_app";
    public static final String CLIENT_SECRET = "123456";
    public static final String GRANT_TYPE = "password";
    String access_token;

    @Autowired
    MockMvc mvc;

    @Autowired
    private RestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        access_token = obtainAccessToken(USERNAME, PASSWORD);
    }

    String obtainAccessToken(String username, String password) throws Exception {
        String url = "http://uaa/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        MultiValueMap<String, Object> parammap = new LinkedMultiValueMap<>();
        parammap.add("grant_type", GRANT_TYPE);
        parammap.add("username", username);
        parammap.add("password", password);
        HttpEntity<Map> entity = new HttpEntity<>(parammap, headers);

        String result = restTemplate.postForObject(url, entity, String.class);

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(result).get("access_token").toString();
    }
}
