package me.batizhao.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.ims.api.vo.UserVO;
import me.batizhao.ims.domain.User;

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
