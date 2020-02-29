package io.github.batizhao.service.iml;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.batizhao.domain.User;
import io.github.batizhao.mapper.UserMapper;
import io.github.batizhao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@Service
public class UserServiceIml extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

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
    public Boolean saveOrUpdate4me(User user) {
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashPass = bcryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(hashPass);
        user.setTime(new Date());

        return saveOrUpdate(user);
    }

//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        User user = this.findByUsername(userName);
//
//        if (user == null) {
//            throw new UsernameNotFoundException(userName);
//        }
//
//        List<Role> roles = roleMapper.findRolesByUserId(user.getId());
//        user.setAuthorities(roles);
//
//        return user;
//    }

}
