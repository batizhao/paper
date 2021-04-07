package io.github.batizhao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.batizhao.domain.RoleMenu;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-09-14
 **/
public interface RoleMenuService extends IService<RoleMenu> {

    /**
     * 更新角色菜单
     * @param roleMenuList
     * @return
     */
    Boolean updateRoleMenus(List<RoleMenu> roleMenuList);

}
