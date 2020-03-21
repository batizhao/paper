package me.batizhao.ims.web.unit;

import me.batizhao.common.core.util.ResultEnum;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.service.RoleService;
import me.batizhao.ims.web.RoleContorller;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @since 2020-02-10
 */
@WebMvcTest(RoleContorller.class)
public class RoleControllerUnitTest extends BaseControllerUnitTest {

    /**
     * 控制扫描范围，否则会加载 Security Config，导致 UserDetailsService 实例化
     */
    @SpringBootApplication(scanBasePackages = {"me.batizhao.ims.web"})
    static class InnerConfig {}

    @Autowired
    private MockMvc mvc;

    /**
     * 所有实现的接口都要 Mock
     */
    @MockBean
    private RoleService roleService;

    private List<RoleVO> roleList;

    /**
     * Prepare test data.
     */
    @Before
    public void setUp() {
        roleList = new ArrayList<>();
        roleList.add(new RoleVO().setId(1L).setName("admin"));
        roleList.add(new RoleVO().setId(2L).setName("common"));
    }

    @Test
    @WithMockUser
    public void givenUserId_whenFindRole_thenRoleJsonArray() throws Exception {
        when(roleService.findRolesByUserId(any())).thenReturn(roleList);

        mvc.perform(get("/role").param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name", equalTo("admin")));

        verify(roleService).findRolesByUserId(any());
    }

    @Test
    @WithMockUser
    public void givenUserId_whenFindRole_thenFail() throws Exception {
        roleList.clear();
        when(roleService.findRolesByUserId(any())).thenReturn(roleList);

        mvc.perform(get("/role").param("userId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(0)));

        verify(roleService).findRolesByUserId(any());
    }
}