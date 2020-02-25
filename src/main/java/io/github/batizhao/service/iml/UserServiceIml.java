package io.github.batizhao.service.iml;

import io.github.batizhao.domain.User;
import io.github.batizhao.repository.UserRepository;
import io.github.batizhao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@Service
public class UserServiceIml implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findOne(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        user.setTime(new Date());
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        user.setTime(new Date());
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Iterable<User> findByName(String name) {
        return userRepository.findByName(name);
    }

}
