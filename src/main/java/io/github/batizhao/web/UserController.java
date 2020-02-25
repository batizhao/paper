package io.github.batizhao.web;

import io.github.batizhao.domain.User;
import io.github.batizhao.exception.ResponseInfo;
import io.github.batizhao.exception.ResultEnum;
import io.github.batizhao.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
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

    @ApiOperation(value = "根据用户名查询用户", notes = "用户名不重复，返回用户详情")
    @GetMapping("username")
    public ResponseInfo<User> findByUsername(@ApiParam(value = "用户名", required = true)
                                             @RequestParam @Size(min = 3) String username) {
        User user = userService.findByUsername(username);
        return new ResponseInfo<User>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(user);
    }

    @ApiOperation(value = "根据姓名查询用户", notes = "有可能重复，所以返回用户列表")
    @GetMapping("name")
    public ResponseInfo<Iterable<User>> findByName(@ApiParam(value = "用户姓名", required = true)
                                                   @RequestParam("name") @Size(min = 2) String name) {
        Iterable<User> users = userService.findByName(name);
        return new ResponseInfo<Iterable<User>>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(users);
    }

    @ApiOperation(value = "列表查询", notes = "返回所有的用户")
    @GetMapping
    public ResponseInfo<Iterable<User>> findAll() {
        Iterable<User> users = userService.findAll();
        return new ResponseInfo<Iterable<User>>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(users);
    }

    @ApiOperation(value = "添加或修改用户", notes = "根据是否有ID判断是添加还是修改")
    @PostMapping
    public ResponseInfo<User> doSaveOrUpdate(@Valid @ApiParam(value = "用户", required = true) @RequestBody User request_user) {
        User user;
        if (request_user.getId() != null) {
            user = userService.update(request_user);
        } else {
            user = userService.save(request_user);
        }
        return new ResponseInfo<User>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(user);
    }

    @ApiOperation(value = "删除用户", notes = "根据用户ID删除用户")
    @DeleteMapping("{id}")
    public ResponseInfo doDelete(@ApiParam(value = "用户ID", required = true) @Min(1) @PathVariable Long id) {
        userService.delete(id);
        return new ResponseInfo().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage());
    }
}
