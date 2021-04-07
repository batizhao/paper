package io.github.batizhao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.batizhao.domain.CodeMeta;

import java.util.List;

/**
 * 生成代码元数据表
 *
 * @author batizhao
 * @since 2021-02-01
 */
public interface CodeMetaService extends IService<CodeMeta> {

    /**
     * 通过 codeId 查询表信息
     * @param codeId
     * @return
     */
    List<CodeMeta> findByCodeId(Long codeId);

    /**
     * 查询表原始信息
     * @param tableName 表名
     * @param dsName 动态数据源名
     * @return
     */
    List<CodeMeta> findColumnsByTableName(String tableName, String dsName);

}
