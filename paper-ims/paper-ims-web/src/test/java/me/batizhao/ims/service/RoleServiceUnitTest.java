package me.batizhao.ims.service;

import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.domain.Role;
import me.batizhao.ims.mapper.RoleMapper;
import me.batizhao.ims.service.iml.RoleServiceIml;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * @author batizhao
 * @since 2020-02-08
 */
@Slf4j
public class RoleServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public RoleService userService() {
            return new RoleServiceIml();
        }
    }

    @MockBean
    private RoleMapper roleMapper;

    @Autowired
    private RoleService roleService;

    private List<Role> roleList;

    /**
     * Prepare test data.
     */
    @Before
    public void setUp() {
        roleList = new ArrayList<>();
        roleList.add(new Role().setId(1L).setName("admin"));
        roleList.add(new Role().setId(2L).setName("common"));
    }

    @Test
    public void givenUserId_whenFindRoles_thenSuccess() {
        when(roleMapper.findRolesByUserId(any()))
                .thenReturn(roleList);

        List<RoleVO> roles = roleService.findRolesByUserId(1L);

        log.info("roles: {}", roles);

        assertThat(roles, hasSize(2));
        assertThat(roles, hasItems(hasProperty("name", is("admin"))));
    }
}