package io.github.batizhao.service;

import io.github.batizhao.domain.User;

import java.util.Optional;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface UserService {

	User findByUsername(String username);

	Iterable<User> findAll();

	Optional<User> findOne(Long id);

	User save(User user);

	User update(User user);

	void delete(Long id);

	Iterable<User> findByName(String name);

}
