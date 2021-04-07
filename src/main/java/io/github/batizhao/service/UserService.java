package io.github.batizhao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.batizhao.domain.User;
import io.github.batizhao.domain.UserInfoVO;

import java.util.List;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface UserService extends IService<User> {

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param user 用户
	 * @return IPage<User>
	 */
	IPage<User> findUsers(Page<User> page, User user);

	/**
	 * 通过id查询用户
	 * @param id id
	 * @return User
	 */
	User findById(Long id);

	/**
	 * 通过 username 查询用户
	 * @param username
	 * @return
	 */
	User findByUsername(String username);

	/**
	 * 通过 name 查询用户
	 * @param name
	 * @return
	 */
//	List<User> findByName(String name);

	/**
	 * 添加或编辑用户
	 * @param user 用户
	 * @return User
	 */
	User saveOrUpdateUser(User user);

	/**
	 * 更新用户密码
	 * @param userId
	 * @param oldPassword
	 * @param newPassword
	 * @return
	 */
	Boolean updatePassword(Long userId, String oldPassword, String newPassword);

	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	Boolean deleteByIds(List<Long> ids);

	/**
	 * 更新用户状态
	 * @param user 用户
	 * @return Boolean
	 */
	Boolean updateStatus(User user);

	/**
	 * 通过id查询用户信息
	 * @param userId
	 * @return
	 */
    UserInfoVO getUserInfo(Long userId);

}
