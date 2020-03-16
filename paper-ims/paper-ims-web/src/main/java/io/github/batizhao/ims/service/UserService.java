package io.github.batizhao.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.batizhao.ims.core.vo.UserVO;
import io.github.batizhao.ims.domain.User;

import java.util.List;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface UserService extends IService<User> {

	UserVO findByUsername(String username);

	List<UserVO> findByName(String name);

	List<UserVO> findAll();

	UserVO findById(Long id);

	int deleteByUsername(String username);

	UserVO saveOrUpdate4me(User user);
}
