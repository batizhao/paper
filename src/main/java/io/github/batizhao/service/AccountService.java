package io.github.batizhao.service;

import io.github.batizhao.domain.Account;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface AccountService {

	Account findByUsername(String username);

	Iterable<Account> findAll();

	Account save(Account account);

	Account findOne(Long id);

	Account update(Account account);

	void delete(Long id);
}
