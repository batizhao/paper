package io.github.batizhao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.batizhao.domain.Role;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
public interface RoleService extends IService<Role> {

    /**
     * 分页查询
     * @param page 分页对象
     * @param role 角色
     * @return IPage<Role>
     */
    IPage<Role> findRoles(Page<Role> page, Role role);

    /**
     * 通过id查询角色
     * @param id id
     * @return Role
     */
    Role findById(Long id);

    /**
     * 添加或编辑角色
     * @param role 角色
     * @return Role
     */
    Role saveOrUpdateRole(Role role);

    /**
     * 删除
     * @param ids
     * @return
     */
    Boolean deleteByIds(List<Long> ids);

    /**
     * 更新角色状态
     * @param role 角色信息
     * @return Boolean
     */
    Boolean updateStatus(Role role);

    /**
     * 通过用户 ID 查相关的角色
     * @param userId
     * @return
     */
    List<Role> findRolesByUserId(Long userId);
}
