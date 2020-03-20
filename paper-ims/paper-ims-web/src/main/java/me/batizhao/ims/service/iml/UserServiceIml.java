package me.batizhao.ims.service.iml;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.common.core.util.BeanCopyUtil;
import me.batizhao.ims.api.vo.UserVO;
import me.batizhao.ims.domain.User;
import me.batizhao.ims.mapper.UserMapper;
import me.batizhao.ims.service.UserService;
import org.springframework.beans.BeanUtils;
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
    private UserMapper userMapper;

    @Override
    public UserVO findByUsername(String username) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));

        if(user == null) {
            throw new NotFoundException(String.format("没有该用户 '%s'。", username));
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> findByName(String name) {
        List<User> userList = userMapper.selectList(Wrappers.<User>lambdaQuery().eq(User::getName, name));
        return BeanCopyUtil.copyListProperties(userList, UserVO::new);
    }

    @Override
    public List<UserVO> findAll() {
        List<User> userList = userMapper.selectList(null);
        return BeanCopyUtil.copyListProperties(userList, UserVO::new);
    }

    @Override
    public UserVO findById(Long id) {
        User user = userMapper.selectById(id);

        if(user == null) {
            throw new NotFoundException(String.format("没有该用户 '%s'。", id));
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public int deleteByUsername(String username) {
        return userMapper.delete(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    @Override
    public UserVO saveOrUpdate4me(User user) {
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashPass = bcryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(hashPass);
        user.setTime(new Date());

        if (user.getId() == null) {
            userMapper.insert(user);
        } else {
            userMapper.updateById(user);
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        return userVO;
    }
}
