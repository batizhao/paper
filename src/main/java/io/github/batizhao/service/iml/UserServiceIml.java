package io.github.batizhao.service.iml;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.batizhao.domain.Role;
import io.github.batizhao.domain.User;
import io.github.batizhao.mapper.RoleMapper;
import io.github.batizhao.mapper.UserMapper;
import io.github.batizhao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@Service
public class UserServiceIml extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;

    @Override
    public User findByUsername(String username) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    @Override
    public List<User> findByName(String name) {
        return userMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getName, name));
    }

    @Override
    public int deleteByUsername(String username) {
        return userMapper.delete(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = this.findByUsername(userName);

        if (user == null) {
            throw new UsernameNotFoundException(userName);
        }

        List<Role> roles = roleMapper.findRolesByUserId(user.getId());
        user.setAuthorities(roles);

        return user;
    }

}
