package io.github.batizhao.web;

import io.github.batizhao.domain.User;
import io.github.batizhao.exception.ResponseInfo;
import io.github.batizhao.exception.ResultEnum;
import io.github.batizhao.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
 * @module Paper
 *
 * @author batizhao
 * @since 2016/9/28
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("user")
@Slf4j
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据用户名查询用户
     * 用户名不重复，返回单个用户详情
     *
     * @param username 用户名
     * @return 用户详情
     */
    @ApiOperation(value = "根据用户名查询用户")
    @GetMapping("username")
    @PreAuthorize("hasRole('USER')")
    public ResponseInfo<User> findByUsername(@ApiParam(value = "用户名", required = true)
                                             @RequestParam @Size(min = 3) String username) {
        User user = userService.findByUsername(username);
        return new ResponseInfo<User>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(user);
    }

    /**
     * 根据姓名查询用户
     * 有可能重复，所以返回用户列表
     *
     * @param name 用户姓名
     * @return 返回用户列表
     */
    @ApiOperation(value = "根据姓名查询用户")
    @GetMapping("name")
    public ResponseInfo<List<User>> findByName(@ApiParam(value = "用户姓名", required = true)
                                                   @RequestParam("name") @Size(min = 2) String name) {
        List<User> users = userService.findByName(name);
        return new ResponseInfo<List<User>>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(users);
    }

    /**
     * 根据ID查询用户
     * 返回用户详情
     *
     * @param id 用户id
     * @return 用户详情
     */
    @ApiOperation(value = "根据ID查询用户")
    @GetMapping("{id}")
    public ResponseInfo<User> findById(@ApiParam(value = "用户ID", required = true) @PathVariable("id") @Min(1) Long id) {
        User user = userService.getById(id);
        return new ResponseInfo<User>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(user);
    }

    /**
     * 查询所有用户
     * 返回所有的用户
     *
     * @return 所有用户列表
     */
    @ApiOperation(value = "查询所有用户")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfo<List<User>> findAll() {
        List<User> users = userService.list();
        return new ResponseInfo<List<User>>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(users);
    }

    /**
     * 添加或修改用户
     * 根据是否有ID判断是添加还是修改
     *
     * @param request_user 用户属性
     * @return 用户对象
     */
    @ApiOperation(value = "添加或修改用户")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfo<User> doSaveOrUpdate(@Valid @ApiParam(value = "用户", required = true) @RequestBody User request_user) {
        User u = userService.saveOrUpdate4me(request_user);
        return new ResponseInfo<User>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(u);
    }

    /**
     * 删除用户
     * 根据用户ID删除用户
     *
     * @param id 用户id
     * @return 成功或者失败
     */
    @ApiOperation(value = "删除用户")
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseInfo<Boolean> doDelete(@ApiParam(value = "用户ID", required = true) @Min(1) @PathVariable Long id) {
        Boolean b = userService.removeById(id);
        return new ResponseInfo<Boolean>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(b);
    }

    /**
     * 我是谁
     * 从认证信息中提取当前用户
     *
     * @param authentication Spring Security Authentication
     * @param authHeader Bearer Access Token
     * @return 用户名
     */
    @ApiOperation(value = "我是谁")
    @GetMapping("/whoiam")
    public ResponseInfo<String> getCurrentUser(Authentication authentication,
                                               @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("authHeader: {}", authHeader);
        String access_token = authHeader.substring("Bearer ".length());
        log.info("access token: {}", access_token);

        String username = authentication.getName();
        return new ResponseInfo<String>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(username);
    }
}
