package io.github.batizhao.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.github.batizhao.util.ResponseInfo;
import io.github.batizhao.aspect.SystemLog;
import io.github.batizhao.domain.Log;
import io.github.batizhao.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 日志管理
 * 使用 AOP 实现 API 日志记录
 *
 * @module pecado-system
 *
 * @author batizhao
 * @since 2020-03-24
 **/
@Api(tags = "日志管理")
@RestController
@Validated
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 分页查询日志
     * @param page 分页对象
     * @param log 日志
     * @return ResponseInfo
     */
    @ApiOperation(value = "分页查询日志")
    @GetMapping("/logs")
    @PreAuthorize("@pms.hasPermission('system:log:admin')")
    public ResponseInfo<IPage<Log>> handleLogs(Page<Log> page, Log log) {
        return ResponseInfo.ok(logService.findLogs(page, log));
    }

    /**
     * 通过id查询日志
     * @param id id
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id查询日志")
    @GetMapping("/log/{id}")
    @PreAuthorize("@pms.hasPermission('system:log:admin')")
    public ResponseInfo<Log> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return ResponseInfo.ok(logService.findById(id));
    }

    /**
     * 添加日志
     * @param log 日志
     * @return ResponseInfo
     */
    @ApiOperation(value = "添加日志")
    @PostMapping("/log")
    public ResponseInfo<Boolean> handleSaveOrUpdate(@Valid @ApiParam(value = "日志" , required = true) @RequestBody Log log) {
        return ResponseInfo.ok(logService.save(log));
    }

    /**
     * 通过id删除日志
     * @param ids ID串
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id删除日志")
    @DeleteMapping(value = "/log", params = "ids")
    @PreAuthorize("@pms.hasPermission('system:log:delete')")
    @SystemLog
    public ResponseInfo<Boolean> handleDelete(@ApiParam(value = "ID串" , required = true) @RequestParam List<Long> ids) {
        return ResponseInfo.ok(logService.removeByIds(ids));
    }

    /**
     * 清空日志
     * @return ResponseInfo
     */
    @ApiOperation(value = "清空日志")
    @DeleteMapping("/log")
    @PreAuthorize("@pms.hasPermission('system:log:clean')")
    @SystemLog
    public ResponseInfo<Boolean> handleDeleteAllLog() {
        return ResponseInfo.ok(logService.remove(null));
    }
}
