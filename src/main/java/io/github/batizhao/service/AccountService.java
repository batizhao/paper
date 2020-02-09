package io.github.batizhao.service;

import io.github.batizhao.domain.Account;

import java.util.Optional;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface AccountService {

	Account findByUsername(String username);

	Iterable<Account> findAll();

	Account save(Account account);

	Optional<Account> findOne(Long id);

	Account update(Account account);

	void delete(Long id);

	Iterable<Account> findByRoles(String role);

}
