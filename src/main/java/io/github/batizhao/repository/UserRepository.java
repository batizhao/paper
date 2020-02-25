package io.github.batizhao.repository;

import io.github.batizhao.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * @author batizhao
 * @since 2016/9/28
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    Iterable<User> findByName(String name);

    /**
     * 这里演示一个事务型的接口
     */
    @Modifying
    @Query(value = "update user set name = :name where id= :id", nativeQuery = true)
    @Transactional
    int updateUserById(@Param("id") Long id, @Param("name") String name);

    /**
     * 这里演示一个事务型的接口
     */
    @Modifying
    @Query(value = "delete from user where username= :username", nativeQuery = true)
    @Transactional
    int deleteUserByUsername(@Param("username") String username);

}
