package io.github.batizhao.service.iml;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.batizhao.domain.UserRole;
import io.github.batizhao.mapper.UserRoleMapper;
import io.github.batizhao.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-09-14
 **/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    @Transactional
    public Boolean updateUserRoles(List<UserRole> userRoles) {
        this.remove(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userRoles.get(0).getUserId()));
        return saveBatch(userRoles);
    }
}
