package io.github.batizhao.service.iml;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.batizhao.constant.MenuTypeEnum;
import io.github.batizhao.exception.NotFoundException;
import io.github.batizhao.util.TreeUtil;
import io.github.batizhao.domain.Menu;
import io.github.batizhao.domain.RoleMenu;
import io.github.batizhao.domain.MetaVO;
import io.github.batizhao.mapper.MenuMapper;
import io.github.batizhao.service.MenuService;
import io.github.batizhao.service.RoleMenuService;
import io.github.batizhao.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<Menu> findMenusByRoleId(Long roleId) {
        List<Menu> menus = menuMapper.findMenusByRoleId(roleId);
        for (Menu menu : menus) {
            menu.setMeta(new MetaVO(menu.getName(), menu.getIcon(), true));
        }
        return menus;
    }

    @Override
    public Set<Menu> findMenusByUserId(Long userId) {
        Set<Menu> all = new HashSet<>();
        roleService.findRolesByUserId(userId).forEach(role -> all.addAll(findMenusByRoleId(role.getId())));
        return all;
    }

    @Override
    public List<Menu> findMenuTreeByUserId(Long userId) {
        return filterMenu(this.findMenusByUserId(userId), null);
    }

    @Override
    public List<Menu> findMenuTree(Menu menu) {
        LambdaQueryWrapper<Menu> wrapper = Wrappers.lambdaQuery();
        if (null != menu && StringUtils.isNotBlank(menu.getName())) {
            wrapper.like(Menu::getName, menu.getName());
        }
        wrapper.orderByAsc(Menu::getSort);

        List<Menu> menus = menuMapper.selectList(wrapper);
//        List<MenuTree> menuTrees = new ArrayList<>();
//        MenuTree menuTree;
//        for (Menu menu : menus) {
//            menuTree = new MenuTree();
//            menuTree.setTitle(menu.getName());
//            menuTree.setKey(menu.getId().toString());
//            menuTree.setPid(menu.getPid());
//            menuTree.setId(menu.getId());
//            menuTrees.add(menuTree);
//        }
        return TreeUtil.build(menus, 0);
    }

    @Override
    public List<Menu> filterMenu(Set<Menu> all, Integer parentId) {
        List<Menu> menuTreeList = all.stream()
                .filter(menu -> MenuTypeEnum.MENU.getType().equals(menu.getType()))
                .sorted(Comparator.comparingInt(Menu::getSort))
                .collect(Collectors.toList());

        Integer parent = parentId == null ? 0 : parentId;
        return TreeUtil.build(menuTreeList, parent);
    }

    @Override
    public Menu findMenuById(Integer id) {
        Menu menu = menuMapper.selectById(id);
        if (menu == null) {
            throw new NotFoundException(String.format("没有该记录 '%s'。", id));
        }
        return menu;
    }

    @Override
    @Transactional
    public Menu saveOrUpdateMenu(Menu menu) {
        if (menu.getId() == null) {
            menu.setCreateTime(LocalDateTime.now());
            menu.setUpdateTime(LocalDateTime.now());
            menuMapper.insert(menu);
        } else {
            menu.setUpdateTime(LocalDateTime.now());
            menuMapper.updateById(menu);
        }

        return menu;
    }

    @Override
    @Transactional
    public Boolean deleteById(Integer id) {
        if (checkHasChildren(id)) return false;

        this.removeById(id);
        roleMenuService.remove(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getMenuId, id));
        return true;
    }

    @Override
    @Transactional
    public Boolean updateStatus(Menu menu) {
        LambdaUpdateWrapper<Menu> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Menu::getId, menu.getId()).set(Menu::getStatus, menu.getStatus());
        return menuMapper.update(null, wrapper) == 1;
    }

    @Override
    public Boolean checkHasChildren(Integer id) {
        return menuMapper.selectList(Wrappers.<Menu>lambdaQuery().eq(Menu::getPid, id)).size() > 0;
    }
}
