package io.github.batizhao.uaa;

import io.github.batizhao.common.core.util.ResponseInfo;
import io.github.batizhao.common.core.util.ResultEnum;
import io.github.batizhao.ims.core.vo.RoleVO;
import io.github.batizhao.ims.core.vo.UserVO;
import io.github.batizhao.uaa.feign.UserFeignService;
import io.github.batizhao.uaa.security.MyUserDetailsServiceImpl;
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

    @Test
    public void givenUserName_whenFindUser_thenSuccess() {
        String username = "zhangsan";
        UserVO user_test_data = new UserVO().setId(1L).setUsername(username).setPassword("123456");
        ResponseInfo<UserVO> userResponseInfo = new ResponseInfo<UserVO>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(user_test_data);

        ArrayList<RoleVO> roleList = new ArrayList<>();
        roleList.add(new RoleVO().setId(1L).setName("admin"));
        roleList.add(new RoleVO().setId(2L).setName("common"));

        ResponseInfo<List<RoleVO>> roleListResponseInfo = new ResponseInfo<List<RoleVO>>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(roleList);

        when(userFeignService.getByUsername(any()))
                .thenReturn(userResponseInfo);

        when(userFeignService.getRolesByUserId(user_test_data.getId()))
                .thenReturn(roleListResponseInfo);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        log.debug("userDetails: {}", userDetails);
        assertThat(userDetails.getUsername(), equalTo(username));

        //TODO 不安全的类型转换
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) userDetails.getAuthorities();
        log.debug("authorities: {}", authorities);
        assertThat(authorities, hasSize(2));

        List<String> list = authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
        assertThat(list, hasItems("admin", "common"));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void givenUserName_whenFindUser_thenUsernameNotFoundException() {
        ResponseInfo<UserVO> userResponseInfo = new ResponseInfo<UserVO>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(null);

        doReturn(userResponseInfo).when(userFeignService).getByUsername(any());

        userDetailsService.loadUserByUsername("xxxx");

        verify(userFeignService).getByUsername(any());
    }

}