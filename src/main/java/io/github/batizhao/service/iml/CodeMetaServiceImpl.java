package io.github.batizhao.service.iml;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.batizhao.domain.CodeMeta;
import io.github.batizhao.mapper.CodeMetaMapper;
import io.github.batizhao.service.CodeMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 生成代码元数据表
 *
 * @author batizhao
 * @since 2021-02-01
 */
@Service
public class CodeMetaServiceImpl extends ServiceImpl<CodeMetaMapper, CodeMeta> implements CodeMetaService {

    @Autowired
    private CodeMetaMapper codeMetaMapper;

    @Override
    public List<CodeMeta> findByCodeId(Long codeId) {
        return codeMetaMapper.selectList(Wrappers.<CodeMeta>query().lambda().eq(CodeMeta::getCodeId, codeId).orderByAsc(CodeMeta::getSort));
    }

    @Override
    @DS("#last")
    public List<CodeMeta> findColumnsByTableName(String tableName, String dsName) {
        return codeMetaMapper.selectColumnsByTableName(tableName);
    }
}
