package me.batizhao.ims.mapper;

import me.batizhao.ims.domain.Role;
import me.batizhao.ims.domain.RolePermission;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Slf4j
public class RoleMapperUnitTest extends BaseMapperUnitTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testFindRolesByUserId() {
        List<Role> roles = roleMapper.findRolesByUserId(1L);

        assertThat(roles, hasItem(allOf(hasProperty("id", is(1L)),
                hasProperty("name", is("ROLE_USER")))));
    }

    @Test
    public void testFindRolePermissions() {
        List<RolePermission> rolePermissions = roleMapper.findRolePermissions();

        log.info("rolePermissions: {}", rolePermissions);

        assertThat(rolePermissions, hasItem(allOf(hasProperty("roleName", is("ROLE_USER")),
                hasProperty("url", is("/user/common")))));

        assertThat(rolePermissions, hasItem(allOf(hasProperty("roleName", is("ROLE_ADMIN")),
                hasProperty("url", is("/user/admin")))));
    }

}
