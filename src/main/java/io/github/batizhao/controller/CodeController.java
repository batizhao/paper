package io.github.batizhao.controller;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import io.github.batizhao.constant.PecadoConstants;
import io.github.batizhao.util.ResponseInfo;
import io.github.batizhao.domain.Code;
import io.github.batizhao.domain.CodeMeta;
import io.github.batizhao.service.CodeMetaService;
import io.github.batizhao.service.CodeService;
import io.github.batizhao.aspect.SystemLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 生成代码
 * 调用这个类根据 table 生成前后端代码
 *
 * @author batizhao
 * @date 2020/10/10
 */
@Api(tags = "生成代码管理")
@RestController
@Slf4j
@Validated
public class CodeController {

    @Autowired
    private CodeService codeService;
    @Autowired
    private CodeMetaService codeMetaService;

    /**
     * 分页查询代码
     * @param page 分页对象
     * @param code 生成代码
     * @return ResponseInfo
     */
    @ApiOperation(value = "分页查询代码")
    @GetMapping("/codes")
    @PreAuthorize("@pms.hasPermission('dp:code:admin')")
    public ResponseInfo<IPage<Code>> handleCodes(Page<Code> page, Code code) {
        return ResponseInfo.ok(codeService.findCodes(page, code));
    }


    /**
     * 通过id查询生成代码
     * @param id id
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id查询代码")
    @GetMapping("/code/{id}")
    @PreAuthorize("@pms.hasPermission('dp:code:admin')")
    public ResponseInfo<Map<String, Object>> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        Code code = codeService.findById(id);
        List<CodeMeta> codeMetas = codeMetaService.findByCodeId(id);
        List<Code> codes = codeService.list()
                .stream().filter(c -> !c.getTableName().equals(code.getTableName()))
                .collect(Collectors.toList());;

        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("codeMetas", codeMetas);
        map.put("codes", codes);
        return ResponseInfo.ok(map);
    }

    /**
     * 修改生成代码
     * @param code 生成代码
     * @return ResponseInfo
     */
    @ApiOperation(value = "修改生成代码")
    @PostMapping("/code")
    @PreAuthorize("@pms.hasPermission('dp:code:edit')")
    @SystemLog
    public ResponseInfo<Code> handleUpdate(@Valid @ApiParam(value = "生成代码" , required = true) @RequestBody Code code) {
        return ResponseInfo.ok(codeService.saveOrUpdateCode(code));
    }

    /**
     * 通过id删除生成代码
     * @param ids ID串
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id删除生成代码")
    @DeleteMapping("/code")
    @PreAuthorize("@pms.hasPermission('dp:code:delete')")
    @SystemLog
    public ResponseInfo<Boolean> handleDelete(@ApiParam(value = "ID串" , required = true) @RequestParam List<Long> ids) {
        return ResponseInfo.ok(codeService.deleteByIds(ids));
    }

    /**
     * 查询数据源下的所有表
     *
     * @param page   分页对象
     * @param code   生成代码
     * @param dsName 数据源
     * @return ResponseInfo
     */
    @ApiOperation(value = "查询数据源下的所有表")
    @GetMapping("/code/tables")
    @PreAuthorize("@pms.hasPermission('dp:code:admin')")
    @SystemLog
    public ResponseInfo<IPage<Code>> handleCodeTables(Page<Code> page, Code code, String dsName) {
        return ResponseInfo.ok(codeService.findTables(page, code, dsName));
    }

    /**
     * 导入选中的表
     * @param codes 表元数据
     * @return
     */
    @ApiOperation(value = "导入选中的表")
    @PostMapping("/code/table")
    @PreAuthorize("@pms.hasPermission('dp:code:import')")
    @SystemLog
    public ResponseInfo<Boolean> handleImportTables(@RequestBody List<Code> codes) {
        return ResponseInfo.ok(codeService.importTables(codes));
    }

    /**
     * 生成代码 zip
     * @param ids
     * @param response
     */
    @SneakyThrows
    @ApiOperation(value = "生成代码zip")
    @PostMapping(value = "/code/zip")
    @PreAuthorize("@pms.hasPermission('dp:code:gen')")
    @SystemLog
    public void handleGenerateCode4Zip(@ApiParam(value = "ID串" , required = true) @RequestParam List<Long> ids, HttpServletResponse response) {
        byte[] data = codeService.downloadCode(ids);
        response.reset();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                String.format("attachment; filename=%s.zip", PecadoConstants.BACK_END_PROJECT));
        response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));
        response.setContentType("application/octet-stream; charset=UTF-8");

        IoUtil.write(response.getOutputStream(), true, data);
    }

    /**
     * 生成代码 path
     * @param id Code Id
     * @return
     */
    @ApiOperation(value = "生成代码path")
    @PostMapping("/code/path/{id}")
    @PreAuthorize("@pms.hasPermission('dp:code:gen')")
    @SystemLog
    public ResponseInfo<Boolean> handleGenerateCode4Path(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return ResponseInfo.ok(codeService.generateCode(id));
    }

    /**
     * 预览代码
     * @param id Code Id
     * @return
     */
    @ApiOperation(value = "预览代码")
    @GetMapping("/code/preview/{id}")
    @PreAuthorize("@pms.hasPermission('dp:code:preview')")
    public ResponseInfo<Map<String, String>> handlePreviewCode(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return ResponseInfo.ok(codeService.previewCode(id));
    }

    /**
     * 同步表元数据
     * @param id Code Id
     * @return
     */
    @ApiOperation(value = "同步表元数据")
    @PostMapping("/code/sync/{id}")
    @PreAuthorize("@pms.hasPermission('dp:code:sync')")
    @SystemLog
    public ResponseInfo<Boolean> handleSyncCodeMeta(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return ResponseInfo.ok(codeService.syncCodeMeta(id));
    }
}
