package io.github.batizhao.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import io.github.batizhao.util.ResponseInfo;
import io.github.batizhao.aspect.SystemLog;
import io.github.batizhao.domain.DictType;
import io.github.batizhao.service.DictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 字典类型 API
 *
 * @module system
 *
 * @author batizhao
 * @since 2021-02-07
 */
@Api(tags = "字典类型管理")
@RestController
@Slf4j
@Validated
public class DictTypeController {

    @Autowired
    private DictTypeService dictTypeService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param dictType 字典类型
     * @return ResponseInfo
     */
    @ApiOperation(value = "分页查询字典类型")
    @GetMapping("/dict/types")
    @PreAuthorize("@pms.hasPermission('system:dict:admin')")
    @SystemLog
    public ResponseInfo<IPage<DictType>> handleDictTypes(Page<DictType> page, DictType dictType) {
        return ResponseInfo.ok(dictTypeService.findDictTypes(page, dictType));
    }

    /**
     * 查询所有
     * @return ResponseInfo
     */
    @ApiOperation(value = "查询所有字典类型")
    @GetMapping("/dict/type")
    @SystemLog
    public ResponseInfo<List<DictType>> handleDictType() {
        return ResponseInfo.ok(dictTypeService.list());
    }

    /**
     * 通过id查询字典类型
     * @param id id
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id查询字典类型")
    @GetMapping("/dict/type/{id}")
    @SystemLog
    public ResponseInfo<DictType> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        DictType dictType = dictTypeService.findById(id);
//        dictType.setDictDataList(dictDataService.list(Wrappers.<DictData>lambdaQuery().eq(DictData::getCode, dictType.getCode())));
        return ResponseInfo.ok(dictType);
    }

    /**
     * 添加或修改字典类型
     * @param dictType 字典类型
     * @return ResponseInfo
     */
    @ApiOperation(value = "添加或修改字典类型")
    @PostMapping("/dict/type")
    @PreAuthorize("@pms.hasPermission('system:dict:add') or @pms.hasPermission('system:dict:edit')")
    @SystemLog
    public ResponseInfo<DictType> handleSaveOrUpdate(@Valid @ApiParam(value = "字典类型" , required = true) @RequestBody DictType dictType) {
        return ResponseInfo.ok(dictTypeService.saveOrUpdateDictType(dictType));
    }

    /**
     * 通过id删除字典类型
     * @param ids ID串
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id删除字典类型")
    @DeleteMapping("/dict/type")
    @PreAuthorize("@pms.hasPermission('system:dict:delete')")
    @SystemLog
    public ResponseInfo<Boolean> handleDelete(@ApiParam(value = "ID串" , required = true) @RequestParam List<Long> ids) {
        return ResponseInfo.ok(dictTypeService.removeByIds(ids));
    }

    /**
     * 更新字典类型状态
     *
     * @param dictType 字典类型
     * @return ResponseInfo
     */
    @ApiOperation(value = "更新字典类型状态")
    @PostMapping("/dict/type/status")
    @PreAuthorize("@pms.hasPermission('system:dict:admin')")
    @SystemLog
    public ResponseInfo<Boolean> handleUpdateStatus(@ApiParam(value = "字典类型" , required = true) @RequestBody DictType dictType) {
        return ResponseInfo.ok(dictTypeService.updateStatus(dictType));
    }

}
