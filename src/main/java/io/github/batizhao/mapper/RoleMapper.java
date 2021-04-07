package io.github.batizhao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.batizhao.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 查用户角色
     * @param id
     * @return
     */
    @Select("SELECT A.id, A.name, A.code FROM role A LEFT JOIN user_role B ON A.id = B.roleId WHERE B.userId = #{id}")
    List<Role> findRolesByUserId(@Param("id") Long id);

//    /**
//     * 查询角色权限关系
//     *
//     * @return
//     */
//    @Select("SELECT A.code AS roleCode,C.path FROM role AS A LEFT JOIN role_menu B ON A.id=B.roleId LEFT JOIN menu AS C ON B.menuId=C.id")
//    List<RoleMenu> findRoleMenus();
}
