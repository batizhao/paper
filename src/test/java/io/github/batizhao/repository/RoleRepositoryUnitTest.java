package io.github.batizhao.repository;

import io.github.batizhao.domain.Role;
import io.github.batizhao.domain.RolePermission;
import io.github.batizhao.mapper.RoleMapper;
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
public class RoleRepositoryUnitTest extends BaseRepositoryUnitTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testFindRolesByUserId() {
        List<Role> roles = roleMapper.findRolesByUserId(1L);

        assertThat(roles, hasItem(allOf(hasProperty("id", is(1L)),
                hasProperty("name", is("USER")))));
    }

    @Test
    public void testFindRolePermissions() {
        List<RolePermission> rolePermissions = roleMapper.findRolePermissions();

        log.info("rolePermissions: {}", rolePermissions);

        assertThat(rolePermissions, hasItem(allOf(hasProperty("roleName", is("USER")),
                hasProperty("url", is("/user/common")))));

        assertThat(rolePermissions, hasItem(allOf(hasProperty("roleName", is("ADMIN")),
                hasProperty("url", is("/user/admin")))));
    }

}
