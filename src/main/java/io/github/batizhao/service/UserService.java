package io.github.batizhao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.batizhao.domain.User;

import java.util.List;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface UserService extends IService<User> {

	User findByUsername(String username);

	List<User> findByName(String name);

	int deleteByUsername(String username);

    User saveOrUpdate4me(User user);
}
