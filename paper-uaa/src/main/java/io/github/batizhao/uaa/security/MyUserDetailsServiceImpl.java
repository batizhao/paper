package io.github.batizhao.uaa.security;

import io.github.batizhao.common.core.util.ResponseInfo;
import io.github.batizhao.ims.core.vo.RoleVO;
import io.github.batizhao.ims.core.vo.UserVO;
import io.github.batizhao.uaa.feign.UserFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author batizhao
 * @since 2020-02-29
 */
@Component
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserFeignService userFeignService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseInfo<UserVO> userData = userFeignService.getByUsername(username);
        UserVO user = userData.getData();

        if (StringUtils.isEmpty(user)) {
            throw new UsernameNotFoundException(String.format("没有该用户 '%s'。", username));
        }

        ResponseInfo<List<RoleVO>> rolesData = userFeignService.getRolesByUserId(user.getId());
        List<RoleVO> roles = rolesData.getData();

        Set<GrantedAuthority> authorities = new HashSet<>();
        roles.forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getName())));

        return new org.springframework.security.core.userdetails.User(
                username, user.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }
}
