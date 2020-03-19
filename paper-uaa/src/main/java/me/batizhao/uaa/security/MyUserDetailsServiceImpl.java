package me.batizhao.uaa.security;

import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.ims.core.vo.RoleVO;
import me.batizhao.ims.core.vo.UserVO;
import me.batizhao.uaa.feign.UserFeignService;
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
        ResponseInfo<UserVO> userData = userFeignService.getByUsername(username, SecurityConstants.FROM_IN);

        if (userData == null || StringUtils.isEmpty(userData.getData())) {
            throw new UsernameNotFoundException(String.format("没有该用户 '%s'。", username));
        }

        UserVO user = userData.getData();

        ResponseInfo<List<RoleVO>> rolesData = userFeignService.getRolesByUserId(user.getId(), SecurityConstants.FROM_IN);

        if (rolesData == null) {
            throw new NullPointerException(String.format("获取 '%s' 时发生错误！", username));
        }

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
