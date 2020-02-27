package io.github.batizhao.repository;

import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 这里最好不要使用 @SpringBootTest，@MybatisTest 保证测试只在 Mapper 层扫描
 *
 * @author batizhao
 * @since 2020-02-07
 */

@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class BaseRepositoryUnitTest {
}
