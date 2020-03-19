package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import me.batizhao.ims.domain.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

/**
 * @author batizhao
 * @since 2020-02-07
 */
public class UserMapperUnitTest extends BaseMapperUnitTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testFindByName() {
        //查找老师
        List<User> users = userMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getName, "孙波波"));
        //确认数量
        assertThat(users, hasSize(2));

        //确认记录内容，这里注意 AnyOrder、Order、allOf、anyOf 的区别
        assertThat(users, containsInAnyOrder(allOf(hasProperty("id", is(5L)),
                                                      hasProperty("email", is("bob@qq.com")),
                                                      hasProperty("username", is("bob"))),
                                                allOf(hasProperty("id", is(6L)),
                                                      hasProperty("email", is("bob2@qq.com")),
                                                      hasProperty("username", is("bob2")))));

        //匹配任意记录中单个属性
        assertThat(users, hasItem(hasProperty("username", is("bob"))));

        //匹配单条记录中的多个属性
        assertThat(users, hasItem(allOf(hasProperty("id", is(5L)),
                                         hasProperty("email", is("bob@qq.com")),
                                         hasProperty("username", is("bob")))));

    }

    /**
     * 这里如果不想影响数据，就要使用 @Transactional，默认 rollback
     */
    @Test
    public void testUpdateUserById() {
        //先确定记录存在
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, "tom"));
        assertThat(user, notNullValue());
        assertThat(user.getEmail(), equalTo("tom@qq.com"));

        //修改
        int result = userMapper.updateUserNameById(2L, "tom22");

        //确认修改成功
        assertThat(result, is(1));
    }

    @Test
    public void testDeleteUserById() {
        String username = "tom";

        //先确定记录存在
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
        assertThat(user, notNullValue());

        //删除
        int result = userMapper.deleteUserByUsername(username);

        //确认删除成功
        assertThat(result, is(1));
    }
}