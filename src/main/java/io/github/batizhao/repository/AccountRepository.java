package io.github.batizhao.repository;

import io.github.batizhao.domain.Account;
import org.springframework.data.repository.CrudRepository;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findByUsername(String username);

}
