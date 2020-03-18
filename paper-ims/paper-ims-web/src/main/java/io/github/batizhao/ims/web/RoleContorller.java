package io.github.batizhao.ims.web;

import io.github.batizhao.common.core.util.ResponseInfo;
import io.github.batizhao.common.core.util.ResultEnum;
import io.github.batizhao.common.security.annotation.Inner;
import io.github.batizhao.ims.core.vo.RoleVO;
import io.github.batizhao.ims.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * 角色管理
 * 这里是角色管理接口的描述
 *
 * @module Paper
 *
 * @author batizhao
 * @since 2020-03-14
 **/
@Api(tags = "角色管理")
@RestController
@RequestMapping("role")
@Slf4j
@Validated
public class RoleContorller {

    @Autowired
    private RoleService roleService;

    /**
     * 根据用户ID查询角色
     * 返回角色集合
     *
     * @param userId 用户id
     * @return 角色集合
     */
    @ApiOperation(value = "根据用户ID查询角色")
    @GetMapping(params = "userId")
    @Inner
    public ResponseInfo<List<RoleVO>> findRolesByUserId(@ApiParam(value = "用户ID", required = true) @RequestParam("userId") @Min(1) Long userId) {
        List<RoleVO> roles = roleService.findRolesByUserId(userId);
        return new ResponseInfo<List<RoleVO>>().setCode(ResultEnum.SUCCESS.getCode())
                .setMessage(ResultEnum.SUCCESS.getMessage())
                .setData(roles);
    }
}
