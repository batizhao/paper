package io.github.batizhao.service;

import io.github.batizhao.domain.Role;
import io.github.batizhao.domain.User;
import io.github.batizhao.mapper.RoleMapper;
import io.github.batizhao.mapper.UserMapper;
import io.github.batizhao.service.iml.UserServiceIml;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

/**
 * @author batizhao
 * @since 2020-02-08
 */
@Slf4j
public class UserServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceIml();
        }
    }

    @MockBean
    private UserMapper userMapper;
    @MockBean
    private RoleMapper roleMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsService userDetailsService;

    private List<User> userList;

    private List<Role> roleList;

    /**
     * Prepare test data.
     */
    @Before
    public void setUp() {
        userList = new ArrayList<>();
        userList.add(new User().setId(1L).setEmail("zhangsan@gmail.com").setUsername("zhangsan").setName("张三").setPassword("123456"));
        userList.add(new User().setId(2L).setEmail("lisi@gmail.com").setUsername("lisi").setName("李四").setPassword("123456"));
        userList.add(new User().setId(3L).setEmail("wangwu@gmail.com").setUsername("wangwu").setName("王五").setPassword("123456"));

        roleList = new ArrayList<>();
        roleList.add(new Role().setId(1L).setName("admin"));
        roleList.add(new Role().setId(2L).setName("common"));
    }

    @Test
    public void givenUserName_thenFindUser_returnUser() {
        String username = "zhangsan";

        when(userMapper.selectOne(any()))
                .thenReturn(userList.get(0));

        User user = userService.findByUsername(username);

        assertThat(user.getUsername(), equalTo(username));
        assertThat(user.getEmail(), equalTo("zhangsan@gmail.com"));
    }

    @Test
    public void givenName_thenFindUser_returnUserList() {
        String name = "张三";

        when(userMapper.selectList(any())).thenReturn(userList.subList(0,1));

        List<User> users = userService.findByName(name);

        verify(userMapper).selectList(any());

        log.info("users: {}", users);

        assertThat(users, hasSize(1));
        assertThat(users, hasItems(hasProperty("username", is("zhangsan"))));
    }

    @Test
    public void givenUserName_thenDeleteUser_returnSucceed() {
        String username = "zhangsan";

        when(userMapper.delete(any()))
                .thenReturn(1);

        int result = userService.deleteByUsername(username);

        assertThat(result, equalTo(1));
    }

    @Test
    public void givenUserJson_thenSaveOrUpdateUser_returnSucceed() {
        User user_test_data = userList.get(0);

        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashPass = bcryptPasswordEncoder.encode(user_test_data.getPassword());
        log.info("hashPass: {}", hashPass);

        boolean bool = bcryptPasswordEncoder.matches(user_test_data.getPassword(), hashPass);
        assertThat(bool, equalTo(true));

        user_test_data.setPassword(hashPass);
        user_test_data.setTime(new Date());
        log.info("user_test_data: {}", user_test_data);

        //这里注意 saveOrUpdate 是第三方的方法，所以用了 spy 对 UserService 做了个 mock
        //并且这里只能使用 doReturn...when 的方式，不能使用 when...thenReturn
        UserService userService = spy(new UserServiceIml());
        doReturn(true).when(userService).saveOrUpdate(user_test_data);

        Boolean result = userService.saveOrUpdate4me(user_test_data);

        verify(userService).saveOrUpdate(any());
        assertThat(result, equalTo(true));
    }

    @Test
    public void givenUserName_thenFindUser_returnUserDetails() {
        String username = "zhangsan";
        User user_test_data = userList.get(0);

        when(userMapper.selectOne(any()))
                .thenReturn(user_test_data);

        when(roleMapper.findRolesByUserId(user_test_data.id))
                .thenReturn(roleList);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        log.debug("userDetails: {}", userDetails);
        assertThat(userDetails.getUsername(), equalTo(username));

        List<Role> authorities = (List<Role>) userDetails.getAuthorities();
        log.debug("authorities: {}", authorities);

        assertThat(authorities, hasSize(2));

        assertThat(authorities, hasItem(allOf(hasProperty("id", is(1L)),
                hasProperty("name", is("admin")))));

        assertThat(authorities, hasItem(allOf(hasProperty("id", is(2L)),
                hasProperty("name", is("common")))));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void givenUserName_thenFindUser_returnUsernameNotFoundException() {
        doReturn(null).when(userMapper).selectOne(any());

        userDetailsService.loadUserByUsername("xxxx");

        verify(userMapper).selectOne(any());
    }

//    @Test
//    public void testFindAll() {
//        when(userMapper.selectList(null))
//                .thenReturn(userList);
//
//        Iterable<User> users = userService.findAll();
//
//        assertThat(users, IsIterableWithSize.iterableWithSize(3));
//        assertThat(users, hasItems(hasProperty("username", is("zhangsan")),
//                                      hasProperty("email", is("lisi@gmail.com")),
//                                      hasProperty("email", is("wangwu@gmail.com"))));
//
//        assertThat(users, containsInAnyOrder(allOf(hasProperty("email", is("zhangsan@gmail.com")),
//                                                      hasProperty("username", is("zhangsan"))),
//                                                allOf(hasProperty("email", is("lisi@gmail.com")),
//                                                      hasProperty("username", is("lisi"))),
//                                                allOf(hasProperty("email", is("wangwu@gmail.com")),
//                                                      hasProperty("username", is("wangwu")))));
//
//    }

//    @Test
//    public void testFindOne() {
//        when(userMapper.selectById(1L))
//                .thenReturn(userList.get(0));
//
//        User user = userService.findOne(1L);
//
//        assertThat(user.getUsername(), equalTo("zhangsan"));
//        assertThat(user.getEmail(), equalTo("zhangsan@gmail.com"));
//    }

//    @Test
//    public void testSave() {
//        User user_test_data = new User().setEmail("zhaoliu@gmail.com").setUsername("zhaoliu");
//
//        when(userMapper.insert(any()))
//                .thenReturn(1);
//
//        int result = userService.save(user_test_data);
//
//        // 验证 userMapper.save() 方法被调用过一次
//        verify(userMapper).insert(any());
//
//        assertThat(result, equalTo(1));
//    }
//
//    @Test
//    public void testUpdate() {
//        User user_test_data = new User().setEmail("zhaoliu@gmail.com").setUsername("zhaoliu");
//
//        when(userMapper.updateById(any()))
//                .thenReturn(1);
//
//        int result = userService.update(user_test_data);
//
//        // 验证 userMapper.save() 方法被调用过一次
//        verify(userMapper).updateById(any());
//
//        assertThat(result, equalTo(1));
//    }

//    @Test
//    public void testDelete() {
//        Long id = 1L;
//
//        //对数据集进行条件过滤
//        doAnswer(invocation -> {
//            Object arg0 = invocation.getArgument(0);
//
//            userList = userList.stream()
//                    .filter(p -> p.getId() != arg0).collect(Collectors.toList());
//
//            return userList;
//        }).when(userMapper).deleteById(id);
//
//        userService.deleteById(id);
//
//        verify(userMapper).deleteById(any());
//
//        assertThat(userList, hasSize(2));
//        assertThat(userList, not(hasItem(hasProperty("id", is(id)))));
//    }

}