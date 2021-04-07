package io.github.batizhao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.batizhao.domain.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 通过角色ID查询菜单
     * @param roleId 角色ID
     * @return
     */
    @Select("SELECT A.* FROM menu A LEFT JOIN role_menu B ON A.id = B.menuId WHERE B.roleId = #{id}")
    List<Menu> findMenusByRoleId(Long roleId);
}
