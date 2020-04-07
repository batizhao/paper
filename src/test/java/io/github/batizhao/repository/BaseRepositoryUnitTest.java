package io.github.batizhao.repository;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author batizhao
 * @since 2020-02-07
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public abstract class BaseRepositoryUnitTest {
}
