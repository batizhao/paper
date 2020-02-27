package io.github.batizhao.web.integration;

import io.github.batizhao.PaperApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *  在集成测试中，不再需要 Mock Bean 和 Stub，
 *  但是需要实例化整个上下文，再对返回数据进行判断
 *  参数校验相关的测试可以放在集成测试中，因为不需要 Stub Data
 *
 * @author batizhao
 * @since 2020-02-07
 */

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = PaperApplication.class)
@AutoConfigureMockMvc
//@TestPropertySource(
//        locations = "classpath:application-integrationtest.properties")
public abstract class BaseControllerIntegrationTest {
}
