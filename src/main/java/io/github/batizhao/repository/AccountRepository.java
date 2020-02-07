package io.github.batizhao.repository;

import io.github.batizhao.domain.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findByUsername(String username);

    Iterable<Account> findByRoles(String role);

    /**
     * 这里演示一个事务型的接口
     */
    @Modifying
    @Query(value = "update account set name = :name where id= :id", nativeQuery = true)
    @Transactional
    int updateUserById(@Param("id") Long id, @Param("name") String name);

    /**
     * 这里演示一个事务型的接口
     */
    @Modifying
    @Query(value = "delete from account where username= :username", nativeQuery = true)
    @Transactional
    int deleteUserByUsername(@Param("username") String username);

}
