package io.github.batizhao.service;

import io.github.batizhao.domain.Account;
import io.github.batizhao.repository.AccountRepository;
import io.github.batizhao.service.iml.AccountServiceIml;
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
public class AccountServiceUnitTest {

    /**
     * Spring Boot provides @TestConfiguration annotation that can be used on classes in src/test/java
     * to indicate that they should not be picked up by scanning.
     */
    @TestConfiguration
    static class AccountServiceTestContextConfiguration {

        @Bean
        public AccountService accountService() {
            return new AccountServiceIml();
        }
    }

    @MockBean
    private AccountRepository accountRepository;

    @Autowired
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
    public void whenUserName_thenAccountShouldBeFound() {
        String username = "zhangsan";

        Mockito.when(accountRepository.findByUsername(username))
                .thenReturn(accountData.iterator().next());

        Account account = accountService.findByUsername(username);

        assertThat(account.getUsername(), equalTo(username));
        assertThat(account.getEmail(), equalTo("zhangsan@gmail.com"));
    }

    @Test
    public void testFindAll() {
        Mockito.when(accountRepository.findAll())
                .thenReturn(accountData);

        Iterable<Account> accounts = accountService.findAll();

        assertThat(accounts, IsIterableWithSize.iterableWithSize(3));
        assertThat(accounts, hasItems(hasProperty("username", is("zhangsan")),
                                      hasProperty("email", is("lisi@gmail.com")),
                                      hasProperty("email", is("wangwu@gmail.com"))));

        assertThat(accounts, containsInAnyOrder(allOf(hasProperty("email", is("zhangsan@gmail.com")),
                                                      hasProperty("username", is("zhangsan"))),
                                                allOf(hasProperty("email", is("lisi@gmail.com")),
                                                      hasProperty("username", is("lisi"))),
                                                allOf(hasProperty("email", is("wangwu@gmail.com")),
                                                      hasProperty("username", is("wangwu")))));

    }

    @Test
    public void testFindOne() {
        Mockito.when(accountRepository.findById(1L))
                .thenReturn(Optional.ofNullable(accountData.iterator().next()));

        Optional<Account> account = accountService.findOne(1L);

        assertThat(account.get().getUsername(), equalTo("zhangsan"));
        assertThat(account.get().getEmail(), equalTo("zhangsan@gmail.com"));
    }

    @Test
    public void testSave() {
        Account account_test_data = Account.builder().email("zhaoliu@gmail.com").username("zhaoliu").build();

        Mockito.when(accountRepository.save(Mockito.any()))
                .thenReturn(account_test_data);

        Account account_return_data = accountService.save(account_test_data);

        // 验证 accountRepository.save() 方法被调用过一次
        Mockito.verify(accountRepository).save(Mockito.any());

        assertThat(account_return_data, notNullValue());
        assertThat(account_return_data.getEmail(), equalTo("zhaoliu@gmail.com"));
    }

    @Test
    public void testUpdate() {
        Account account_test_data = Account.builder().email("zhaoliu@gmail.com").username("zhaoliu").build();

        Mockito.when(accountRepository.save(Mockito.any()))
                .thenReturn(account_test_data);

        Account account_return_data = accountService.update(account_test_data);

        // 验证 accountRepository.save() 方法被调用过一次
        Mockito.verify(accountRepository).save(Mockito.any());

        assertThat(account_return_data, notNullValue());
        assertThat(account_return_data.getEmail(), equalTo("zhaoliu@gmail.com"));
    }

    @Test
    public void testDelete() {
        Long id = 1L;

        //对数据集进行条件过滤
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);

            accountList = accountList.stream()
                    .filter(p -> p.getId() != arg0).collect(Collectors.toList());

            return accountList;
        }).when(accountRepository).deleteById(id);

        accountService.delete(1L);

        Mockito.verify(accountRepository).deleteById(Mockito.any());

        assertThat(accountList, IsIterableWithSize.iterableWithSize(2));
        assertThat(accountList, not(hasItem(hasProperty("id", is(id)))));
    }

    @Test
    public void testFindByRoles() {
        String role = "admin";

        //对数据集进行条件过滤
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);

            accountList = accountList.stream()
                    .filter(p -> p.getRoles().equals(arg0)).collect(Collectors.toList());

            return accountList;
        }).when(accountRepository).findByRoles(role);

        Iterable<Account> accounts = accountService.findByRoles(role);

        assertThat(accounts, IsIterableWithSize.iterableWithSize(1));
        assertThat(accounts, hasItems(hasProperty("username", is("zhangsan"))));
    }
}