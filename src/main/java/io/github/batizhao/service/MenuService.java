package io.github.batizhao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.batizhao.domain.Menu;

import java.util.List;
import java.util.Set;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
public interface MenuService extends IService<Menu> {

    /**
     * 通过角色查询权限
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<Menu> findMenusByRoleId(Long roleId);

    /**
     * 查询当前用户菜单列表
     * 返回菜单树
     *
     * @return 菜单树
     */
    Set<Menu> findMenusByUserId(Long userId);

    /**
     * 查询当前用户菜单树
     * 返回菜单树
     *
     * @return 菜单树
     */
    List<Menu> findMenuTreeByUserId(Long userId);

    /**
     * 查询所有菜单
     * @return 菜单树
     */
    List<Menu> findMenuTree(Menu menu);

    /**
     * 构造菜单树
     * @param all 全部菜单
     * @param parentId 父节点ID
     * @return 菜单树
     */
    List<Menu> filterMenu(Set<Menu> all, Integer parentId);

    /**
     * 查询菜单
     * @param id
     * @return 菜单对象
     */
    Menu findMenuById(Integer id);

    /**
     * 添加或者更新菜单
     * @param menu
     * @return
     */
    Menu saveOrUpdateMenu(Menu menu);

    /**
     * 删除
     * @param id
     * @return
     */
    Boolean deleteById(Integer id);

    /**
     * 更新菜单状态
     * @param menu 菜单信息
     * @return Boolean
     */
    Boolean updateStatus(Menu menu);

    /**
     * 检查是否有子菜单
     * 有返回 true，无返回 false
     *
     * @param id
     * @return
     */
    Boolean checkHasChildren(Integer id);
}
