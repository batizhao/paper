package io.github.batizhao.web;

import io.github.batizhao.domain.Account;
import io.github.batizhao.exception.ResponseInfo;
import io.github.batizhao.exception.ResultEnum;
import io.github.batizhao.service.AccountService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("account")
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountService;

    @ApiOperation(value = "获取用户详情", notes = "根据用户名返回用户详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "path", dataType = "String")
    })
    @GetMapping("{username}")
    public ResponseInfo findByUsername(@PathVariable String username) {
        Account account = accountService.findByUsername(username);
        return new ResponseInfo().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(account);
    }

    @ApiOperation(value = "列表查询", notes = "返回所有的用户")
    @GetMapping("index")
    public ResponseInfo findAll() {
        Iterable<Account> accounts = accountService.findAll();
        return new ResponseInfo().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(accounts);
    }

    @ApiOperation(value = "添加或修改用户", notes = "根据是否有ID判断是添加还是修改")
    @PostMapping
    public ResponseInfo doSaveOrUpdate(@ApiParam(value = "用户", required = true) @RequestBody Account request_account) {
        Account account;
        if (request_account.getId() != null) {
            account = accountService.update(request_account);
        } else {
            account = accountService.save(request_account);
        }
        return new ResponseInfo().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(account);
    }

    @ApiOperation(value = "删除用户", notes = "根据用户ID删除用户")
    @DeleteMapping("{id:\\d+}")
    public ResponseInfo doDelete(@ApiParam(value = "用户ID", required = true) @PathVariable Long id) {
        accountService.delete(id);
        return new ResponseInfo().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage());
    }

    @ApiOperation(value = "根据角色查询用户", notes = "返回用户列表")
    @GetMapping("role")
    public ResponseInfo doFindByRoles(@ApiParam(value = "用户角色", required = true) @RequestParam("role") String role) {
        Iterable<Account> accounts = accountService.findByRoles(role);
        return new ResponseInfo().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(accounts);
    }

}
