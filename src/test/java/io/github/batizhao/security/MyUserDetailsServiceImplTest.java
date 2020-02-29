package io.github.batizhao.security;

import io.github.batizhao.domain.Role;
import io.github.batizhao.domain.User;
import io.github.batizhao.mapper.RoleMapper;
import io.github.batizhao.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author batizhao
 * @since 2020-02-29
 */
@RunWith(SpringRunner.class)
@Slf4j
public class MyUserDetailsServiceImplTest {

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public UserDetailsService userDetailsService() {
            return new MyUserDetailsServiceImpl();
        }
    }

    @MockBean
    private UserMapper userMapper;
    @MockBean
    private RoleMapper roleMapper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    public void givenUserName_thenFindUser_returnUserDetails() {
        String username = "zhangsan";
        User user_test_data = new User().setId(1L).setUsername(username).setPassword("123456");

        ArrayList<Role> roleList = new ArrayList<>();
        roleList.add(new Role().setId(1L).setName("admin"));
        roleList.add(new Role().setId(2L).setName("common"));

        when(userMapper.selectOne(any()))
                .thenReturn(user_test_data);

        when(roleMapper.findRolesByUserId(user_test_data.id))
                .thenReturn(roleList);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        log.debug("userDetails: {}", userDetails);
        assertThat(userDetails.getUsername(), equalTo(username));

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) userDetails.getAuthorities();
        log.debug("authorities: {}", authorities);
        assertThat(authorities, hasSize(2));

        List<String> list = authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
        assertThat(list, hasItems("admin", "common"));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void givenUserName_thenFindUser_returnUsernameNotFoundException() {
        doReturn(null).when(userMapper).selectOne(any());

        userDetailsService.loadUserByUsername("xxxx");

        verify(userMapper).selectOne(any());
    }

}