package io.github.batizhao.service.iml;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.batizhao.exception.NotFoundException;
import io.github.batizhao.domain.Role;
import io.github.batizhao.domain.RoleMenu;
import io.github.batizhao.domain.UserRole;
import io.github.batizhao.mapper.RoleMapper;
import io.github.batizhao.service.RoleMenuService;
import io.github.batizhao.service.RoleService;
import io.github.batizhao.service.UserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public IPage<Role> findRoles(Page<Role> page, Role role) {
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(role.getName())) {
            wrapper.like(Role::getName, role.getName());
        }
        return roleMapper.selectPage(page, wrapper);
    }

    @Override
    public Role findById(Long id) {
        Role role = roleMapper.selectById(id);

        if(role == null) {
            throw new NotFoundException(String.format("没有该记录 '%s'。", id));
        }

        return role;
    }

    @Override
    @Transactional
    public Role saveOrUpdateRole(Role role) {
        if (role.getId() == null) {
            role.setCreateTime(LocalDateTime.now());
            role.setUpdateTime(LocalDateTime.now());
            roleMapper.insert(role);
        } else {
            role.setUpdateTime(LocalDateTime.now());
            roleMapper.updateById(role);
        }

        return role;
    }

    @Override
    @Transactional
    public Boolean deleteByIds(List<Long> ids) {
        this.removeByIds(ids);
        ids.forEach(i -> {
            userRoleService.remove(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId, i));
            roleMenuService.remove(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, i));
        });
        return true;
    }

    @Override
    @Transactional
    public Boolean updateStatus(Role role) {
        LambdaUpdateWrapper<Role> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Role::getId, role.getId()).set(Role::getStatus, role.getStatus());
        return roleMapper.update(null, wrapper) == 1;
    }

    @Override
    public List<Role> findRolesByUserId(Long userId) {
        return roleMapper.findRolesByUserId(userId);
    }
}
