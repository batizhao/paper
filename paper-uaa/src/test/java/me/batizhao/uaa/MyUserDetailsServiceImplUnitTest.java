package me.batizhao.uaa;

import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.api.vo.UserVO;
import me.batizhao.ims.api.feign.UserFeignService;
import me.batizhao.uaa.security.MyUserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
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
public class MyUserDetailsServiceImplUnitTest {

    @MockBean
    private UserFeignService userFeignService;

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public UserDetailsService userDetailsService() {
            return new MyUserDetailsServiceImpl();
        }
    }

    @Autowired
    private UserDetailsService userDetailsService;

    private List<RoleVO> roleList;

    /**
     * Prepare test data.
     */
    @Before
    public void setUp() {
        roleList = new ArrayList<>();
        roleList.add(new RoleVO().setId(1L).setName("admin"));
        roleList.add(new RoleVO().setId(2L).setName("common"));
    }

    @Test
    public void givenUserName_whenFindUser_thenSuccess() {
        String username = "zhangsan";
        UserVO user_test_data = new UserVO().setId(1L).setUsername(username).setPassword("123456");
        user_test_data.setRoleList(roleList);

        ResponseInfo<UserVO> userResponseInfo = ResponseInfo.ok(user_test_data);

        when(userFeignService.loadUserByUsername(username, SecurityConstants.FROM_IN))
                .thenReturn(userResponseInfo);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        log.debug("userDetails: {}", userDetails);
        assertThat(userDetails.getUsername(), equalTo(username));

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        log.debug("authorities: {}", authorities);
        assertThat(authorities, hasSize(2));

        List<String> list = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        assertThat(list, hasItems("admin", "common"));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void givenUserName_whenFindUser_thenUsernameNotFoundException() {
        ResponseInfo<UserVO> userResponseInfo = ResponseInfo.ok();

        doReturn(userResponseInfo).when(userFeignService).loadUserByUsername(any(), any());

        userDetailsService.loadUserByUsername("xxxx");

        verify(userFeignService).loadUserByUsername(any(), any());
    }

    @Test
    public void givenUserName_whenFindUserRoles_thenFail() {
        String username = "zhangsan";
        UserVO user_test_data = new UserVO().setId(1L).setUsername(username).setPassword("123456");

        roleList.clear();
        user_test_data.setRoleList(roleList);

        ResponseInfo<UserVO> userResponseInfo = ResponseInfo.ok(user_test_data);

        when(userFeignService.loadUserByUsername(username, SecurityConstants.FROM_IN))
                .thenReturn(userResponseInfo);

        userDetailsService.loadUserByUsername(username);

        verify(userFeignService).loadUserByUsername(any(), any());
    }
}