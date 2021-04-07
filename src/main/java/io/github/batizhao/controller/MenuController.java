package io.github.batizhao.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import io.github.batizhao.util.ResponseInfo;
import io.github.batizhao.util.SecurityUtils;
import io.github.batizhao.domain.Menu;
import io.github.batizhao.service.MenuService;
import io.github.batizhao.aspect.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 菜单管理
 * 这里是菜单管理接口的描述
 *
 * @module pecado-ims
 *
 * @author batizhao
 * @since 2020-03-14
 **/
@Api(tags = "菜单管理")
@RestController
@Slf4j
@Validated
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 查询当前用户菜单
     * 返回菜单树
     *
     * @return 菜单树
     */
    @ApiOperation(value = "查询当前用户菜单")
    @GetMapping("/menu/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseInfo<List<Menu>> handleMenuTree4Me() {
        Long userId = SecurityUtils.getUser().getUserId();
        return ResponseInfo.ok(menuService.findMenuTreeByUserId(userId));
    }

    /**
     * 根据角色查询菜单
     * 返回菜单树
     *
     * @return 菜单树
     */
    @ApiOperation(value = "根据角色查询菜单")
    @GetMapping(value = "menu", params = "roleId")
    @PreAuthorize("@pms.hasPermission('ims:menu:admin')")
    public ResponseInfo<List<Menu>> handleMenusByRoleId(@ApiParam(value = "菜单ID", required = true) @RequestParam("roleId") @Min(1) Long roleId) {
        return ResponseInfo.ok(menuService.findMenusByRoleId(roleId));
    }

    /**
     * 查询所有菜单
     * 返回菜单树
     *
     * @return 菜单树
     */
    @ApiOperation(value = "查询所有菜单")
    @GetMapping("/menus")
    @PreAuthorize("isAuthenticated()")
    public ResponseInfo<List<Menu>> handleMenuTree(Menu menu) {
        return ResponseInfo.ok(menuService.findMenuTree(menu));
    }

    /**
     * 通过id查询菜单
     * @param id 菜单 ID
     * @return 菜单对象
     */
    @ApiOperation(value = "通过id查询菜单")
    @GetMapping("/menu/{id}")
    @PreAuthorize("@pms.hasPermission('ims:menu:admin')")
    public ResponseInfo<Menu> handleMenu(@ApiParam(value = "菜单ID", required = true) @PathVariable("id") @Min(1) Integer id) {
        return ResponseInfo.ok(menuService.findMenuById(id));
    }

    /**
     * 添加或修改菜单
     * 根据是否有ID判断是添加还是修改
     *
     * @param menu 菜单属性
     * @return 菜单对象
     */
    @ApiOperation(value = "添加或修改菜单")
    @PostMapping("menu")
    @PreAuthorize("@pms.hasPermission('ims:menu:add') or @pms.hasPermission('ims:menu:edit')")
    @SystemLog
    public ResponseInfo<Menu> handleSaveOrUpdate(@Valid @ApiParam(value = "菜单", required = true) @RequestBody Menu menu) {
        return ResponseInfo.ok(menuService.saveOrUpdateMenu(menu));
    }

    /**
     * 删除菜单
     * 根据菜单ID删除菜单
     *
     * @return 成功或者失败
     */
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("menu")
    @PreAuthorize("@pms.hasPermission('ims:menu:delete')")
    @SystemLog
    public ResponseInfo<String> handleDelete(@ApiParam(value = "菜单ID串", required = true) @RequestParam Integer id) {
        return menuService.deleteById(id) ? ResponseInfo.ok() : ResponseInfo.failed("存在子菜单不允许删除！");
    }

    /**
     * 更新菜单状态
     *
     * @param menu 菜单
     * @return ResponseInfo
     */
    @ApiOperation(value = "更新菜单状态")
    @PostMapping("/menu/status")
    @PreAuthorize("@pms.hasPermission('ims:menu:admin')")
    @SystemLog
    public ResponseInfo<Boolean> handleUpdateStatus(@ApiParam(value = "菜单" , required = true) @RequestBody Menu menu) {
        return ResponseInfo.ok(menuService.updateStatus(menu));
    }

}
