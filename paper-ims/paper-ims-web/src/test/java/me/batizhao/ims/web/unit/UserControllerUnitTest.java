package me.batizhao.ims.web.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.api.vo.UserVO;
import me.batizhao.ims.domain.User;
import me.batizhao.ims.service.RoleService;
import me.batizhao.ims.service.UserService;
import me.batizhao.ims.web.UserController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 注意，在 Spring Security 启用的情况下：
 * 1. post、put、delete 方法要加上 with(csrf())，否则会返回 403，因为这里控制了 config 扫描范围 csrf.disable 并没有生效
 * 2. 单元测试要控制扫描范围，防止 Spring Security Config 自动初始化，尤其是 UserDetailsService 自定义的情况（会加载 Mapper）
 * 3. 测试方法要加上 @WithMockUser，否则会返回 401
 *
 * @author batizhao
 * @since 2020-02-10
 */
@WebMvcTest(UserController.class)
public class UserControllerUnitTest extends BaseControllerUnitTest {

    /**
     * 控制扫描范围，否则会加载 Security Config，导致 UserDetailsService 实例化
     */
    @SpringBootApplication(scanBasePackages = {"me.batizhao.ims.web"})
    static class InnerConfig {}

    @Autowired
    private MockMvc mvc;

    /**
     * 所有实现的接口都要 Mock
     */
    @MockBean
    private UserService userService;
    @MockBean
    private RoleService roleService;

    private List<UserVO> userList;

    /**
     * Prepare test data.
     */
    @Before
    public void setUp() {
        userList = new ArrayList<>();
        userList.add(new UserVO().setId(1L).setEmail("zhangsan@gmail.com").setUsername("zhangsan").setName("张三"));
        userList.add(new UserVO().setId(2L).setEmail("lisi@gmail.com").setUsername("lisi").setName("李四"));
        userList.add(new UserVO().setId(3L).setEmail("wangwu@gmail.com").setUsername("wangwu").setName("王五"));
    }

    @Test
    @WithMockUser
    public void givenUserName_whenFindUser_thenUserJson() throws Exception {
        String username = "zhangsan";

        when(userService.findByUsername(username)).thenReturn(userList.get(0));

        mvc.perform(get("/user/username").param("username", username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.email").value("zhangsan@gmail.com"));

        verify(userService).findByUsername(any());
    }

    @Test
    @WithMockUser
    public void givenUserName_whenFindUser_thenUserDetailJson() throws Exception {
        String username = "zhangsan";

        List<RoleVO> roleList = new ArrayList<>();
        roleList.add(new RoleVO().setId(1L).setName("admin"));
        roleList.add(new RoleVO().setId(2L).setName("common"));

        UserVO userVO = userList.get(0);
        when(userService.findByUsername(username)).thenReturn(userVO);

        userVO.setRoleList(roleList);
        when(roleService.findRolesByUserId(userVO.getId())).thenReturn(roleList);

        mvc.perform(get("/user/userdetail").param("username", username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.email").value("zhangsan@gmail.com"))
                .andExpect(jsonPath("$.data.roleList", hasSize(2)))
                .andExpect(jsonPath("$.data.roleList[1].id").value("2"));

        verify(userService).findByUsername(any());
    }

    @Test
    @WithMockUser
    public void givenName_whenFindUser_thenUserListJson() throws Exception {
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

    @Test
    @WithMockUser
    public void givenId_whenFindUser_thenUserJson() throws Exception {
        Long id = 1L;

        when(userService.findById(id)).thenReturn(userList.get(0));

        mvc.perform(get("/user/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.email").value("zhangsan@gmail.com"));

        verify(userService).findById(any());
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindAllUser_thenUserListJson() throws Exception {
        when(userService.findAll()).thenReturn(userList);

        mvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].username", equalTo("zhangsan")));

        verify(userService).findAll();
    }

    @Test
    @WithMockUser
    public void givenJson_whenSaveUser_thenSucceedJson() throws Exception {
        User requestBody = new User().setEmail("zhaoliu@gmail.com").setUsername("zhaoliu").setPassword("xxx").setName("xxx");

        when(userService.saveOrUpdate4me(any()))
                .thenReturn(userList.get(0));

        mvc.perform(post("/user").with(csrf())
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        verify(userService).saveOrUpdate4me(any());
    }

    @Test
    @WithMockUser
    public void givenJson_whenUpdateUser_thenSucceedJson() throws Exception {
        User requestBody = new User().setId(2L).setEmail("zhaoliu@gmail.com").setUsername("zhaoliu").setPassword("xxx").setName("xxx");

        when(userService.saveOrUpdate4me(any()))
                .thenReturn(userList.get(1));

        mvc.perform(post("/user").with(csrf())
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(2)))
                .andExpect(jsonPath("$.data.username", equalTo("lisi")));

        verify(userService).saveOrUpdate4me(any());
    }


    /**
     * 删除成功的情况
     *
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void givenId_whenDeleteUser_thenSucceed() throws Exception {
        when(userService.removeById(anyLong())).thenReturn(true);

        mvc.perform(delete("/user/{id}", 1L).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(userService).removeById(1L);
    }

}