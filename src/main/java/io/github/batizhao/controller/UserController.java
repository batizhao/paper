package io.github.batizhao.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import io.github.batizhao.util.ResponseInfo;
import io.github.batizhao.util.SecurityUtils;
import io.github.batizhao.domain.User;
import io.github.batizhao.domain.UserRole;
import io.github.batizhao.domain.UserInfoVO;
import io.github.batizhao.service.UserRoleService;
import io.github.batizhao.service.UserService;
import io.github.batizhao.aspect.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 用户管理
 * 这里是用户管理接口的描述
 *
 * @module pecado-ims
 *
 * @author batizhao
 * @since 2016/9/28
 */
@Api(tags = "用户管理")
@RestController
@Slf4j
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param user 用户
     * @return ResponseInfo
     */
    @ApiOperation(value = "分页查询用户")
    @GetMapping("users")
    @PreAuthorize("@pms.hasPermission('ims:user:admin')")
    public ResponseInfo<IPage<User>> handleUsers(Page<User> page, User user) {
        return ResponseInfo.ok(userService.findUsers(page, user));
    }

    /**
     * 通过id查询用户
     * @param id id
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id查询用户")
    @GetMapping("/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseInfo<User> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return ResponseInfo.ok(userService.findById(id));
    }

    /**
     * 根据用户名查询用户
     * 用户名不重复，返回单个用户详情（包括其角色）
     *
     * @param username 用户名
     * @return 用户详情
     */
    @ApiOperation(value = "根据用户名查询用户")
    @GetMapping(value = "user", params = "username")
    @SystemLog
    public ResponseInfo<UserInfoVO> handleUsername(@ApiParam(value = "用户名", required = true) @RequestParam @Size(min = 3) String username) {
        User user = userService.findByUsername(username);
        return ResponseInfo.ok(userService.getUserInfo(user.getId()));
    }

    /**
     * 添加或编辑用户
     * @param user 用户
     * @return ResponseInfo
     */
    @ApiOperation(value = "添加或编辑用户")
    @PostMapping("/user")
    @PreAuthorize("@pms.hasPermission('ims:user:add') or @pms.hasPermission('ims:user:edit')")
    @SystemLog
    public ResponseInfo<User> handleSaveOrUpdate(@Valid @ApiParam(value = "用户" , required = true) @RequestBody User user) {
        return ResponseInfo.ok(userService.saveOrUpdateUser(user));
    }

    /**
     * 删除用户
     * 根据用户ID删除用户
     *
     * @return 成功或者失败
     */
    @ApiOperation(value = "删除用户")
    @DeleteMapping("user")
    @PreAuthorize("@pms.hasPermission('ims:user:delete')")
    @SystemLog
    public ResponseInfo<Boolean> handleDelete(@ApiParam(value = "用户ID串", required = true) @RequestParam List<Long> ids) {
        return ResponseInfo.ok(userService.deleteByIds(ids));
    }

    /**
     * 更新用户状态
     *
     * @param user 用户
     * @return ResponseInfo
     */
    @ApiOperation(value = "更新用户状态")
    @PostMapping("/user/status")
    @PreAuthorize("@pms.hasPermission('ims:user:admin')")
    @SystemLog
    public ResponseInfo<Boolean> handleUpdateStatus(@ApiParam(value = "用户" , required = true) @RequestBody User user) {
        return ResponseInfo.ok(userService.updateStatus(user));
    }

    /**
     * 我的信息
     *
     * @return 当前用户基本信息、角色、权限清单
     */
    @ApiOperation(value = "我的信息")
    @GetMapping("/user/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseInfo<UserInfoVO> handleUserInfo() {
        Long userId = SecurityUtils.getUser().getUserId();
        return ResponseInfo.ok(userService.getUserInfo(userId));
    }

    /**
     * 更换我的头像
     *
     * @param user 用户
     * @return ResponseInfo
     */
    @ApiOperation(value = "更换我的头像")
    @PostMapping("/user/avatar")
    @PreAuthorize("isAuthenticated()")
    @SystemLog
    public ResponseInfo<User> handleUpdateAvatar(@ApiParam(value = "用户" , required = true) @RequestBody User user) {
        Long userId = SecurityUtils.getUser().getUserId();
        return ResponseInfo.ok(userService.saveOrUpdateUser(user.setId(userId)));
    }

    /**
     * 更新我的密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return ResponseInfo
     */
    @ApiOperation(value = "更新我的密码")
    @PostMapping("/user/password")
    @PreAuthorize("isAuthenticated()")
    @SystemLog
    public ResponseInfo<Boolean> handleUpdatePassword(@ApiParam(value = "旧密码" , required = true) @Size(min = 6) @RequestParam String oldPassword,
                                                      @ApiParam(value = "新密码" , required = true) @Size(min = 6) @RequestParam String newPassword) {
        Long userId = SecurityUtils.getUser().getUserId();
        return ResponseInfo.ok(userService.updatePassword(userId, oldPassword, newPassword));
    }

    /**
     * 分配用户角色
     * 返回 true or false
     *
     * @param userRoleList 角色清单
     * @return true or false
     */
    @ApiOperation(value = "分配用户角色")
    @PostMapping(value = "/user/role")
    @PreAuthorize("@pms.hasPermission('ims:user:admin')")
    @SystemLog
    public ResponseInfo<Boolean> handleAddUserRoles(@ApiParam(value = "关联角色", required = true) @RequestBody List<UserRole> userRoleList) {
        return ResponseInfo.ok(userRoleService.updateUserRoles(userRoleList));
    }

}
