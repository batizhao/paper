package io.github.batizhao.service;

import io.github.batizhao.domain.Account;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface AccountService {

	Account findByUsername(String username);

}
