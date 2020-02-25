package io.github.batizhao.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.batizhao.domain.Account;
import io.github.batizhao.exception.ResultEnum;
import io.github.batizhao.service.AccountService;
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
@WebMvcTest(AccountController.class)
public class AccountControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService accountService;

    private Iterable<Account> accountData;

    private List<Account> accountList;

    /**
     * Prepare test data.
     */
    @Before
    public void setUp() {
        accountList = new ArrayList<>();
        accountList.add(Account.builder().id(1L).email("zhangsan@gmail.com").username("zhangsan").roles("admin").build());
        accountList.add(Account.builder().id(2L).email("lisi@gmail.com").username("lisi").roles("teacher").build());
        accountList.add(Account.builder().id(3L).email("wangwu@gmail.com").username("wangwu").roles("student").build());

        accountData = accountList;
    }

    @Test
    public void whenGetAccount_thenReturnJson() throws Exception {
        String username = "zhangsan";

        when(accountService.findByUsername(username)).thenReturn(accountData.iterator().next());

        mvc.perform(get("/account/{username}", username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.email").value("zhangsan@gmail.com"));

        verify(accountService).findByUsername(any());
    }

    @Test
    public void whenGetAccounts_thenReturnJsonArray() throws Exception {
        when(accountService.findAll()).thenReturn(accountData);

        mvc.perform(get("/account/index"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].username", equalTo("zhangsan")));

        verify(accountService).findAll();
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
                .when(accountService)
                .findAll();

        mvc.perform(get("/account/index"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.UNKNOWN_ERROR.getCode()));
    }

    @Test
    public void whenSaveAccount_thenReturnJson() throws Exception {
        String email = "zhaoliu@gmail.com";
        String username = "zhaoliu";
        Account account_test_data = Account.builder().id(1L).email(email).username(username).build();

        when(accountService.save(any()))
                .thenReturn(account_test_data);

        Account requestBody = Account.builder().email(email).username(username)
                .password("xxx").name("xxx").roles("xxx").build();
        mvc.perform(post("/account")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(containsString(username)))
                .andExpect(jsonPath("$.data.email").value(email));

        verify(accountService).save(any());
    }

    @Test
    public void whenUpdateAccount_thenReturnJson() throws Exception {
        String email = "zhaoliu@gmail.com";
        String username = "zhaoliu";
        Account account_test_data = Account.builder().id(1L).email(email).username(username).build();

        when(accountService.update(any()))
                .thenReturn(account_test_data);

        Account requestBody = Account.builder().id(1L).email(email).username(username)
                .password("xxx").name("xxx").roles("xxx").build();
        mvc.perform(post("/account")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(containsString(username)))
                .andExpect(jsonPath("$.data.username").value(username));

        verify(accountService).update(any());
    }


    /**
     * 删除成功的情况
     *
     * @throws Exception
     */
    @Test
    public void whenDeleteAccount_thenReturnTrue() throws Exception {
        doNothing().when(accountService).delete(anyLong());

        mvc.perform(delete("/account/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));

        verify(accountService).delete(1L);
    }

    @Test
    public void whenGetAccountsByRoles_thenReturnJsonArray() throws Exception {
        String role = "admin";

        //对数据集进行条件过滤
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);

            accountList = accountList.stream()
                    .filter(p -> p.getRoles().equals(arg0)).collect(Collectors.toList());

            return accountList;
        }).when(accountService).findByRoles(role);

        mvc.perform(get("/account/role").param("role", role))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].username", equalTo("zhangsan")));

        verify(accountService).findByRoles(role);
    }

    /**
     * TODO 增加权限，重新测试这个异常
     * @throws Exception
     */
    @Test
    public void testGetUser_thenReturnAccessControlException() throws Exception {
        doThrow(AccessControlException.class)
                .when(accountService)
                .findAll();

        mvc.perform(get("/account/index"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PERMISSION_ERROR.getCode()));
    }
}