package io.github.batizhao.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import io.github.batizhao.util.ResponseInfo;
import io.github.batizhao.domain.CodeMeta;
import io.github.batizhao.service.CodeMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * 生成代码元数据 API
 *
 * @module system
 *
 * @author batizhao
 * @since 2021-03-19
 */
@Api(tags = "生成代码元数据管理")
@RestController
@Slf4j
@Validated
public class CodeMetaController {

    @Autowired
    private CodeMetaService codeMetaService;

    /**
     * 通过 codeId 查询生成代码元数据
     * @param codeId code.id
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过 codeId 查询生成代码元数据")
    @GetMapping(value = "/code/meta", params = "codeId")
    public ResponseInfo<List<CodeMeta>> handleCode(@ApiParam(value = "codeId" , required = true) @RequestParam @Min(1) Long codeId) {
        return ResponseInfo.ok(codeMetaService.findByCodeId(codeId));
    }

}