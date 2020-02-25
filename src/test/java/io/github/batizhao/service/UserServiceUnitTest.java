package io.github.batizhao.service;

import io.github.batizhao.domain.User;
import io.github.batizhao.repository.UserRepository;
import io.github.batizhao.service.iml.UserServiceIml;
import org.hamcrest.collection.IsIterableWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doAnswer;

/**
 * @author batizhao
 * @since 2020-02-08
 */
@RunWith(SpringRunner.class)
public class UserServiceUnitTest {

    /**
     * Spring Boot provides @TestConfiguration annotation that can be used on classes in src/test/java
     * to indicate that they should not be picked up by scanning.
     */
    @TestConfiguration
    static class userServiceTestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserServiceIml();
        }
    }

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private Iterable<User> userData;

    private List<User> userList;

    /**
     * Prepare test data.
     */
    @Before
    public void setUp() {
        userList = new ArrayList<>();
        userList.add(User.builder().id(1L).email("zhangsan@gmail.com").username("zhangsan").name("张三").build());
        userList.add(User.builder().id(2L).email("lisi@gmail.com").username("lisi").name("李四").build());
        userList.add(User.builder().id(3L).email("wangwu@gmail.com").username("wangwu").name("王五").build());

        userData = userList;
    }

    @Test
    public void whenUserName_thenAccountShouldBeFound() {
        String username = "zhangsan";

        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(userData.iterator().next());

        User user = userService.findByUsername(username);

        assertThat(user.getUsername(), equalTo(username));
        assertThat(user.getEmail(), equalTo("zhangsan@gmail.com"));
    }

    @Test
    public void testFindAll() {
        Mockito.when(userRepository.findAll())
                .thenReturn(userData);

        Iterable<User> users = userService.findAll();

        assertThat(users, IsIterableWithSize.iterableWithSize(3));
        assertThat(users, hasItems(hasProperty("username", is("zhangsan")),
                                      hasProperty("email", is("lisi@gmail.com")),
                                      hasProperty("email", is("wangwu@gmail.com"))));

        assertThat(users, containsInAnyOrder(allOf(hasProperty("email", is("zhangsan@gmail.com")),
                                                      hasProperty("username", is("zhangsan"))),
                                                allOf(hasProperty("email", is("lisi@gmail.com")),
                                                      hasProperty("username", is("lisi"))),
                                                allOf(hasProperty("email", is("wangwu@gmail.com")),
                                                      hasProperty("username", is("wangwu")))));

    }

    @Test
    public void testFindOne() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(userData.iterator().next()));

        Optional<User> user = userService.findOne(1L);

        assertThat(user.get().getUsername(), equalTo("zhangsan"));
        assertThat(user.get().getEmail(), equalTo("zhangsan@gmail.com"));
    }

    @Test
    public void testSave() {
        User user_test_data = User.builder().email("zhaoliu@gmail.com").username("zhaoliu").build();

        Mockito.when(userRepository.save(Mockito.any()))
                .thenReturn(user_test_data);

        User user_return_data = userService.save(user_test_data);

        // 验证 userRepository.save() 方法被调用过一次
        Mockito.verify(userRepository).save(Mockito.any());

        assertThat(user_return_data, notNullValue());
        assertThat(user_return_data.getEmail(), equalTo("zhaoliu@gmail.com"));
    }

    @Test
    public void testUpdate() {
        User user_test_data = User.builder().email("zhaoliu@gmail.com").username("zhaoliu").build();

        Mockito.when(userRepository.save(Mockito.any()))
                .thenReturn(user_test_data);

        User user_return_data = userService.update(user_test_data);

        // 验证 userRepository.save() 方法被调用过一次
        Mockito.verify(userRepository).save(Mockito.any());

        assertThat(user_return_data, notNullValue());
        assertThat(user_return_data.getEmail(), equalTo("zhaoliu@gmail.com"));
    }

    @Test
    public void testDelete() {
        Long id = 1L;

        //对数据集进行条件过滤
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);

            userList = userList.stream()
                    .filter(p -> p.getId() != arg0).collect(Collectors.toList());

            return userList;
        }).when(userRepository).deleteById(id);

        userService.delete(1L);

        Mockito.verify(userRepository).deleteById(Mockito.any());

        assertThat(userList, IsIterableWithSize.iterableWithSize(2));
        assertThat(userList, not(hasItem(hasProperty("id", is(id)))));
    }

    @Test
    public void testFindByName() {
        String name = "张三";

        //对数据集进行条件过滤
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);

            userList = userList.stream()
                    .filter(p -> p.getName().equals(arg0)).collect(Collectors.toList());

            return userList;
        }).when(userRepository).findByName(name);

        Iterable<User> users = userService.findByName(name);

        assertThat(users, IsIterableWithSize.iterableWithSize(1));
        assertThat(users, hasItems(hasProperty("username", is("zhangsan"))));
    }
}