package io.github.batizhao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.batizhao.domain.Code;
import io.github.batizhao.domain.CodeMeta;

import java.util.List;
import java.util.Map;

/**
 * @author batizhao
 * @date 2020/10/10
 */
public interface CodeService extends IService<Code> {

    /**
     * 分页查询
     * @param page 分页对象
     * @param code 生成代码
     * @return IPage<Code>
     */
    IPage<Code> findCodes(Page<Code> page, Code code);

    /**
     * 通过id查询生成代码
     * @param id id
     * @return Code
     */
    Code findById(Long id);

    /**
     * 添加\生成代码
     * @param code 生成代码
     * @return Code
     */
    Code saveCode(Code code, List<CodeMeta> codeMetas);

    /**
     * 添加或修改生成代码
     * @param code 生成代码
     * @return Code
     */
    Code saveOrUpdateCode(Code code);

    /**
     * 删除
     * @param ids
     * @return
     */
    Boolean deleteByIds(List<Long> ids);

    /**
     * 查询数据源下的所有表
     * @param page 分页对象
     * @param dsName 数据源
     * @return IPage<Code>
     */
    IPage<Code> findTables(Page<Code> page, Code code, String dsName);

    /**
     * 导入选中的表
     * @param codes
     * @return
     */
    Boolean importTables(List<Code> codes);

    /**
     * 生成代码 zip
     * @param ids
     * @return byte[]
     */
    byte[] downloadCode(List<Long> ids);

    /**
     * 生成代码 path
     * @param id
     * @return
     */
    Boolean generateCode(Long id);

    /**
     * 预览代码
     * @param id Code Id
     * @return
     */
    Map<String, String> previewCode(Long id);

    /**
     * 同步表元数据
     * @param id
     * @return
     */
    Boolean syncCodeMeta(Long id);

    /**
     * 同步表列
     * @param id
     * @param codeMetas
     * @param dbTableColumns
     * @return
     */
    Boolean syncColumn(Long id, List<CodeMeta> codeMetas, List<CodeMeta> dbTableColumns);

}
