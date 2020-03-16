package io.github.batizhao.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.batizhao.ims.core.vo.RoleVO;
import io.github.batizhao.ims.domain.Role;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
public interface RoleService extends IService<Role> {

    List<RoleVO> findRolesByUserId(Long userId);

}
