package me.batizhao.ims.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.domain.Role;
import me.batizhao.ims.mapper.RoleMapper;
import me.batizhao.ims.service.RoleService;
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
