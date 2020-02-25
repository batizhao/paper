package io.github.batizhao.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.batizhao.domain.User;
import io.github.batizhao.exception.ResultEnum;
import io.github.batizhao.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @since 2020-02-10
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
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
    public void whenGetUser_thenReturnJson() throws Exception {
        String username = "zhangsan";

        when(userService.findByUsername(username)).thenReturn(userData.iterator().next());

        mvc.perform(get("/user/{username}", username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.email").value("zhangsan@gmail.com"));

        verify(userService).findByUsername(any());
    }

    @Test
    public void whenGetUsers_thenReturnJsonArray() throws Exception {
        when(userService.findAll()).thenReturn(userData);

        mvc.perform(get("/user/index"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].username", equalTo("zhangsan")));

        verify(userService).findAll();
    }

    /**
     * 测试返回 NullPointerException 的情况
     * 通常情况下不用测试这种类型，这里只是需要模拟 NullPointerException，查看返回数据
     * Unchecked exception 使用 Mockito 即可
     * @throws Exception
     */
    @Test
    public void testGetUser_thenReturnNullPointerException() throws Exception {
        doThrow(NullPointerException.class)
                .when(userService)
                .findAll();

        mvc.perform(get("/user/index"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.UNKNOWN_ERROR.getCode()));
    }

    @Test
    public void whenSaveUser_thenReturnJson() throws Exception {
        String email = "zhaoliu@gmail.com";
        String username = "zhaoliu";
        User user_test_data = User.builder().id(1L).email(email).username(username).build();

        when(userService.save(any()))
                .thenReturn(user_test_data);

        User requestBody = User.builder().email(email).username(username)
                .password("xxx").name("xxx").build();
        mvc.perform(post("/user")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(containsString(username)))
                .andExpect(jsonPath("$.data.email").value(email));

        verify(userService).save(any());
    }

    @Test
    public void whenUpdateUser_thenReturnJson() throws Exception {
        String email = "zhaoliu@gmail.com";
        String username = "zhaoliu";
        User user_test_data = User.builder().id(1L).email(email).username(username).build();

        when(userService.update(any()))
                .thenReturn(user_test_data);

        User requestBody = User.builder().id(1L).email(email).username(username)
                .password("xxx").name("xxx").build();
        mvc.perform(post("/user")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(containsString(username)))
                .andExpect(jsonPath("$.data.username").value(username));

        verify(userService).update(any());
    }


    /**
     * 删除成功的情况
     *
     * @throws Exception
     */
    @Test
    public void whenDeleteUser_thenReturnTrue() throws Exception {
        doNothing().when(userService).delete(anyLong());

        mvc.perform(delete("/user/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));

        verify(userService).delete(1L);
    }

    @Test
    public void whenGetUsersByName_thenReturnJsonArray() throws Exception {
        String name = "张三";

        //对数据集进行条件过滤
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);

            userList = userList.stream()
                    .filter(p -> p.getName().equals(arg0)).collect(Collectors.toList());

            return userList;
        }).when(userService).findByName(name);

        mvc.perform(get("/user/name").param("name", name))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].username", equalTo("zhangsan")));

        verify(userService).findByName(name);
    }

    /**
     * TODO 增加权限，重新测试这个异常
     * @throws Exception
     */
    @Test
    public void testGetUser_thenReturnAccessControlException() throws Exception {
        doThrow(AccessControlException.class)
                .when(userService)
                .findAll();

        mvc.perform(get("/user/index"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PERMISSION_ERROR.getCode()));
    }
}