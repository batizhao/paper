package me.batizhao.ims.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.batizhao.ims.domain.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 这里演示一个事务型的接口
     */
    @Update("update user set name = #{name} where id= #{id}")
    int updateUserNameById(@Param("id") Long id, @Param("name") String name);

    /**
     * 这里演示一个事务型的接口
     */
    @Delete("delete from user where username= #{username}")
    int deleteUserByUsername(@Param("username") String username);

}
