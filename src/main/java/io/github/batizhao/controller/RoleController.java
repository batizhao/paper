package io.github.batizhao.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import io.github.batizhao.util.ResponseInfo;
import io.github.batizhao.domain.Role;
import io.github.batizhao.domain.RoleMenu;
import io.github.batizhao.service.RoleMenuService;
import io.github.batizhao.service.RoleService;
import io.github.batizhao.aspect.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 角色管理
 * 这里是角色管理接口的描述
 *
 * @module pecado-ims
 *
 * @author batizhao
 * @since 2020-03-14
 **/
@Api(tags = "角色管理")
@RestController
@Slf4j
@Validated
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 分页查询
     * 返回角色集合
     *
     * @return 角色集合
     */
    @ApiOperation(value = "分页查询角色")
    @GetMapping("/roles")
    @PreAuthorize("@pms.hasPermission('ims:role:admin')")
    public ResponseInfo<IPage<Role>> handleRoles(Page<Role> page, Role role) {
        return ResponseInfo.ok(roleService.findRoles(page, role));
    }

    /**
     * 查询所有角色
     * 返回角色集合
     *
     * @return 角色集合
     */
    @ApiOperation(value = "查询所有角色")
    @GetMapping("/role")
    @PreAuthorize("@pms.hasPermission('ims:role:admin')")
    public ResponseInfo<List<Role>> handleRoles() {
        return ResponseInfo.ok(roleService.list());
    }

    /**
     * 通过id查询角色
     * @param id id
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id查询角色")
    @GetMapping("/role/{id}")
    @PreAuthorize("@pms.hasPermission('ims:role:admin')")
    public ResponseInfo<Role> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return ResponseInfo.ok(roleService.findById(id));
    }

    /**
     * 添加或修改角色
     * 根据是否有ID判断是添加还是修改
     *
     * @param role 角色属性
     * @return 角色对象
     */
    @ApiOperation(value = "添加或修改角色")
    @PostMapping("role")
    @PreAuthorize("@pms.hasPermission('ims:role:add') or @pms.hasPermission('ims:role:edit')")
    @SystemLog
    public ResponseInfo<Role> handleSaveOrUpdate(@Valid @ApiParam(value = "角色", required = true) @RequestBody Role role) {
        return ResponseInfo.ok(roleService.saveOrUpdateRole(role));
    }

    /**
     * 删除角色
     * 根据角色ID删除角色
     *
     * @return 成功或者失败
     */
    @ApiOperation(value = "删除角色")
    @DeleteMapping("role")
    @PreAuthorize("@pms.hasPermission('ims:role:delete')")
    @SystemLog
    public ResponseInfo<Boolean> handleDelete(@ApiParam(value = "角色ID串", required = true) @RequestParam List<Long> ids) {
        return ResponseInfo.ok(roleService.deleteByIds(ids));
    }

    /**
     * 更新角色状态
     *
     * @param role 角色
     * @return ResponseInfo
     */
    @ApiOperation(value = "更新角色状态")
    @PostMapping("/role/status")
    @PreAuthorize("@pms.hasPermission('ims:role:admin')")
    @SystemLog
    public ResponseInfo<Boolean> handleUpdateStatus(@ApiParam(value = "角色" , required = true) @RequestBody Role role) {
        return ResponseInfo.ok(roleService.updateStatus(role));
    }

    /**
     * 根据用户ID查询角色
     * 返回角色集合
     *
     * @param userId 用户id
     * @return 角色集合
     */
    @ApiOperation(value = "根据用户ID查询角色")
    @GetMapping(value = "role", params = "userId")
    @PreAuthorize("@pms.hasPermission('ims:role:admin')")
    public ResponseInfo<List<Role>> handleRolesByUserId(@ApiParam(value = "用户ID", required = true) @RequestParam("userId") @Min(1) Long userId) {
        return ResponseInfo.ok(roleService.findRolesByUserId(userId));
    }

    /**
     * 分配角色权限
     * 返回 true or false
     *
     * @param roleMenuList 权限清单
     * @return true or false
     */
    @ApiOperation(value = "分配角色权限")
    @PostMapping(value = "/role/menu")
    @PreAuthorize("@pms.hasPermission('ims:role:admin')")
    @SystemLog
    public ResponseInfo<Boolean> handleAddUserRoles(@ApiParam(value = "关联菜单", required = true) @RequestBody List<RoleMenu> roleMenuList) {
        return ResponseInfo.ok(roleMenuService.updateRoleMenus(roleMenuList));
    }

}
