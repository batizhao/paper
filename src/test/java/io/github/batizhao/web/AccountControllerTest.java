package io.github.batizhao.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.batizhao.domain.Account;
import io.github.batizhao.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author batizhao
 * @since 2020-02-10
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

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

        Mockito.when(accountService.findByUsername(username)).thenReturn(accountData.iterator().next());

        mvc.perform(get("/account/{username}", username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(username)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("zhangsan@gmail.com"));

    }

    @Test
    public void whenGetAccounts_thenReturnJsonArray() throws Exception {

        Mockito.when(accountService.findAll()).thenReturn(accountData);

        mvc.perform(get("/account"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username", equalTo("zhangsan")));
    }

    @Test
    public void whenSaveAccount_thenReturnJson() throws Exception {
        String email = "zhaoliu@gmail.com";
        String username = "zhaoliu";
        Account account_test_data = Account.builder().id(1L).email(email).username(username).build();

        Mockito.when(accountService.save(Mockito.any()))
                .thenReturn(account_test_data);

        Account requestBody = Account.builder().email(email).username(username).build();
        mvc.perform(post("/account")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(username)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email));
    }

    @Test
    public void whenUpdateAccount_thenReturnJson() throws Exception {
        String email = "zhaoliu@gmail.com";
        String username = "zhaoliu";
        Account account_test_data = Account.builder().id(1L).email(email).username(username).build();

        Mockito.when(accountService.update(Mockito.any()))
                .thenReturn(account_test_data);

        Account requestBody = Account.builder().id(1L).email(email).username(username).build();
        mvc.perform(post("/account")
                .content(new ObjectMapper().writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(username)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email));
    }
}