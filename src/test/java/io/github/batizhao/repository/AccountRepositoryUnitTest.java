package io.github.batizhao.repository;

import io.github.batizhao.domain.Account;
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
public class AccountRepositoryUnitTest extends BaseRepositoryUnitTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void testFindByUsername() {
        Account account = accountRepository.findByUsername("admin");

        assertThat(account.getEmail(), equalTo("batizhao@qq.com"));
        assertThat(account, hasProperty("password", equalTo("7c4a8d09ca3762af61e59520943dc26494f8941b")));
        assertThat(account.getName(), containsString("管理"));
    }

    @Test
    public void testFindByRoles() {
        //查找老师
        Iterable<Account> accounts = accountRepository.findByRoles("teacher");
        //确认数量
        assertThat(accounts, IsIterableWithSize.iterableWithSize(2));

        //确认记录内容，这里注意 AnyOrder、Order、allOf、anyOf 的区别
        assertThat(accounts, containsInAnyOrder(allOf(hasProperty("id", is(2L)),
                                                      hasProperty("name", is("张老师")),
                                                      hasProperty("username", is("zhangsan"))),
                                                allOf(hasProperty("id", is(3L)),
                                                      hasProperty("name", is("李老师")),
                                                      hasProperty("username", is("lisi")))));

        //查找学生
        accounts = accountRepository.findByRoles("student");
        //确认数量
        assertThat(accounts, IsIterableWithSize.iterableWithSize(5));

        //这里演示 List 的用法，所以转成 List
        List<Account> result = StreamSupport.stream(accounts.spliterator(), false).collect(Collectors.toList());

        //匹配任意记录中单个属性
        assertThat(result, hasItem(hasProperty("username", is("wangwu"))));
        assertThat(result, hasItem(hasProperty("name", is("赵六"))));

        //匹配单条记录中的多个属性
        assertThat(result, hasItem(allOf(hasProperty("id", is(4L)),
                                         hasProperty("name", is("王五")),
                                         hasProperty("username", is("wangwu")))));

    }

    /**
     * 这里如果不想影响数据，就要使用 @Transactional，默认 rollback
     */
    @Test
    @Transactional
    public void testUpdateUserById() {
        String username = "wangwu";

        //先确定记录存在
        Account account = accountRepository.findByUsername(username);
        assertThat(account, notNullValue());
        assertThat(account.getEmail(), equalTo("wangwu@qq.com"));

        //修改
        int result = accountRepository.updateUserById(4L, "王五");

        //确认修改成功
        assertThat(result, is(1));
    }

    @Test
    @Transactional
    public void testDeleteUserById() {
        String username = "zhexingsun";

        //先确定记录存在
        Account account = accountRepository.findByUsername(username);
        assertThat(account, notNullValue());

        //删除
        int result = accountRepository.deleteUserByUsername(username);

        //确认删除成功
        assertThat(result, is(1));
    }
}