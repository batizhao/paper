package io.github.batizhao.service.iml;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.batizhao.domain.RoleMenu;
import io.github.batizhao.mapper.RoleMenuMapper;
import io.github.batizhao.service.RoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-09-14
 **/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Override
    @Transactional
    public Boolean updateRoleMenus(List<RoleMenu> roleMenuList) {
        this.remove(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, roleMenuList.get(0).getRoleId()));
        return saveBatch(roleMenuList);
    }
}
