package io.github.batizhao.repository;

import io.github.batizhao.domain.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findByUsername(String username);

    Iterable<Account> findByRoles(String role);
}
