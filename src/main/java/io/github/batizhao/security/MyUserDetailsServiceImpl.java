package io.github.batizhao.security;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.github.batizhao.domain.Role;
import io.github.batizhao.domain.User;
import io.github.batizhao.mapper.RoleMapper;
import io.github.batizhao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    RoleMapper roleMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));

        if (user == null) {
            throw new UsernameNotFoundException(String.format("没有该用户 '%s'。", username));
        }

        List<Role> roles = roleMapper.findRolesByUserId(user.getId());

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
