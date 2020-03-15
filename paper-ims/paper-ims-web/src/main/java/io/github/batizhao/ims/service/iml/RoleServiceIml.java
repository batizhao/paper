package io.github.batizhao.ims.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.batizhao.ims.core.vo.RoleVO;
import io.github.batizhao.ims.core.vo.UserVO;
import io.github.batizhao.ims.entity.Role;
import io.github.batizhao.ims.entity.User;
import io.github.batizhao.ims.mapper.RoleMapper;
import io.github.batizhao.ims.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
@Service
public class RoleServiceIml extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<RoleVO> findRolesByUserId(Long userId) {
        List<Role> roleList = roleMapper.findRolesByUserId(userId);
        List<RoleVO> roles = new ArrayList<RoleVO>();

        if (!CollectionUtils.isEmpty(roleList)) {
            for (Role role : roleList) {
                RoleVO roleVO = new RoleVO();
                BeanUtils.copyProperties(role, roleVO);
                roles.add(roleVO);
            }
        }
        return roles;
    }
}
