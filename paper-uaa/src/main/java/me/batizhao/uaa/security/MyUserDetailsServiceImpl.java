package me.batizhao.uaa.security;

import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.api.vo.UserVO;
import me.batizhao.ims.api.feign.UserFeignService;
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
    public UserDetails loadUserByUsername(String username) {
        ResponseInfo<UserVO> userData = userFeignService.loadUserByUsername(username, SecurityConstants.FROM_IN);

        if (userData == null || StringUtils.isEmpty(userData.getData())) {
            throw new UsernameNotFoundException(String.format("没有该用户 '%s'。", username));
        }

        UserVO user = userData.getData();
        List<RoleVO> roles = user.getRoleList();
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
