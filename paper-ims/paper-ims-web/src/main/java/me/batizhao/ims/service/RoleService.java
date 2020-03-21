package me.batizhao.ims.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.domain.Role;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
public interface RoleService extends IService<Role> {

    List<RoleVO> findRolesByUserId(Long userId);

}
