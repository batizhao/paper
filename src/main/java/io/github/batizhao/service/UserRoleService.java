package io.github.batizhao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.batizhao.domain.UserRole;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-09-14
 **/
public interface UserRoleService extends IService<UserRole> {

    /**
     * 更新用户角色
     * @param userRoles
     * @return
     */
    Boolean updateUserRoles(List<UserRole> userRoles);
}
