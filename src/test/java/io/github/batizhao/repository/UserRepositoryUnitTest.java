package io.github.batizhao.repository;

import io.github.batizhao.domain.User;
import org.hamcrest.collection.IsIterableWithSize;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author batizhao
 * @since 2020-02-07
 */
public class UserRepositoryUnitTest extends BaseRepositoryUnitTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        User user = userRepository.findByUsername("admin");

        assertThat(user.getEmail(), equalTo("admin@qq.com"));
        assertThat(user, hasProperty("password", equalTo("7c4a8d09ca3762af61e59520943dc26494f8941b")));
        assertThat(user.getName(), containsString("管理"));
    }

    @Test
    public void testFindByName() {
        //查找老师
        Iterable<User> users = userRepository.findByName("孙波波");
        //确认数量
        assertThat(users, IsIterableWithSize.iterableWithSize(2));

        //确认记录内容，这里注意 AnyOrder、Order、allOf、anyOf 的区别
        assertThat(users, containsInAnyOrder(allOf(hasProperty("id", is(5L)),
                                                      hasProperty("email", is("bob@qq.com")),
                                                      hasProperty("username", is("bob"))),
                                                allOf(hasProperty("id", is(6L)),
                                                      hasProperty("email", is("bob2@qq.com")),
                                                      hasProperty("username", is("bob2")))));

        //这里演示 List 的用法，所以转成 List
        List<User> result = StreamSupport.stream(users.spliterator(), false).collect(Collectors.toList());

        //匹配任意记录中单个属性
        assertThat(result, hasItem(hasProperty("username", is("bob"))));

        //匹配单条记录中的多个属性
        assertThat(result, hasItem(allOf(hasProperty("id", is(5L)),
                                         hasProperty("email", is("bob@qq.com")),
                                         hasProperty("username", is("bob")))));

    }

    /**
     * 这里如果不想影响数据，就要使用 @Transactional，默认 rollback
     */
    @Test
    @Transactional
    public void testUpdateUserById() {
        String username = "tom";

        //先确定记录存在
        User user = userRepository.findByUsername(username);
        assertThat(user, notNullValue());
        assertThat(user.getEmail(), equalTo("tom@qq.com"));

        //修改
        int result = userRepository.updateUserById(2L, "王五");

        //确认修改成功
        assertThat(result, is(1));
    }

    @Test
    @Transactional
    public void testDeleteUserById() {
        String username = "tom";

        //先确定记录存在
        User user = userRepository.findByUsername(username);
        assertThat(user, notNullValue());

        //删除
        int result = userRepository.deleteUserByUsername(username);

        //确认删除成功
        assertThat(result, is(1));
    }
}