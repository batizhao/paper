package io.github.batizhao.service.iml;

import cn.hutool.core.io.IoUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.batizhao.constant.GenConstants;
import io.github.batizhao.exception.NotFoundException;
import io.github.batizhao.domain.Code;
import io.github.batizhao.domain.CodeMeta;
import io.github.batizhao.mapper.CodeMapper;
import io.github.batizhao.service.CodeMetaService;
import io.github.batizhao.service.CodeService;
import io.github.batizhao.util.CodeGenUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

/**
 * 在这里要注意，在事务开启的情况下，动态数据源会无效。
 * 无论是上级方法，还是当前方法，都不能开启事务。
 *
 * @author batizhao
 * @date 2020/10/10
 */
@Service
public class CodeServiceImpl extends ServiceImpl<CodeMapper, Code> implements CodeService {

    @Autowired
    private CodeMapper codeMapper;
    @Autowired
    private CodeMetaService codeMetaService;

    @Override
    public IPage<Code> findCodes(Page<Code> page, Code code) {
        LambdaQueryWrapper<Code> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(code.getTableName())) {
            wrapper.like(Code::getTableName, code.getTableName());
        }
        return codeMapper.selectPage(page, wrapper);
    }

    @Override
    public Code findById(Long id) {
        Code code = codeMapper.selectById(id);

        if (code == null) {
            throw new NotFoundException(String.format("没有该记录 '%s'。" , id));
        }

        return code;
    }

    /**
     * 当前方法开启事务，会导致动态数据源无法切换。
     * 为了解决这个问题，单独封装了 saveCode 方法处理事务。
     * 但是这种方式，只适合这个方法，并不适合所有的事务+动态数据源的场景。
     * @see <a href="SpringBoot+Mybatis配置多数据源及事务方案">https://juejin.cn/post/6844904159074844685</a>
     *
     * @param code 生成代码
     * @return
     */
    @Override
    public Code saveOrUpdateCode(Code code) {
        if (code.getId() == null) {
            code.setCreateTime(LocalDateTime.now());
            code.setUpdateTime(LocalDateTime.now());
            List<CodeMeta> codeMetas = codeMetaService.findColumnsByTableName(code.getTableName(), code.getDsName());
            saveCode(code, codeMetas);
        } else {
            code.setUpdateTime(LocalDateTime.now());
            this.updateById(code);
            if (CollectionUtils.isNotEmpty(code.getCodeMetaList())) {
                codeMetaService.updateBatchById(code.getCodeMetaList());
            }
        }

        return code;
    }

    @Override
    @Transactional
    public Code saveCode(Code code, List<CodeMeta> codeMetas) {
        this.save(code);
        codeMetas.forEach(cm -> {
            cm.setCodeId(code.getId());
            CodeGenUtils.initColumnField(cm);
        });
        codeMetaService.saveBatch(codeMetas);

        return code;
    }

    @Override
    @Transactional
    public Boolean deleteByIds(List<Long> ids) {
        this.removeByIds(ids);
        ids.forEach(i -> codeMetaService.remove(Wrappers.<CodeMeta>lambdaQuery().eq(CodeMeta::getCodeId, i)));
        return true;
    }

    @Override
    @DS("#last")
    public IPage<Code> findTables(Page<Code> page, Code code, String dsName) {
        IPage<Code> p = codeMapper.selectTablePageByDs(page, code);
        List<Code> c = p.getRecords();

        if (StringUtils.isBlank(dsName)) {
            dsName = "master";
        }

        String finalDsName = dsName;
        c.forEach(ll -> ll.setDsName(finalDsName));

        return p;
    }

    /**
     * 这里不能开启事务，会导致动态数据源失效。
     *
     * @param codes
     * @return
     */
    @Override
    public Boolean importTables(List<Code> codes) {
        if (codes == null) return false;
        for (Code c : codes) {
            CodeGenUtils.initData(c);
            saveOrUpdateCode(c);
        }
        return true;
    }

    @Override
    public byte[] downloadCode(List<Long> ids) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for (Long i : ids) {
            CodeGenUtils.generateCode(prepareCodeMeta(i), zip);
        }

        IoUtil.close(zip);
        return outputStream.toByteArray();
    }

    @Override
    public Boolean generateCode(Long id) {
        CodeGenUtils.generateCode(prepareCodeMeta(id));
        return true;
    }

    @Override
    public Map<String, String> previewCode(Long id) {
        return CodeGenUtils.previewCode(prepareCodeMeta(id));
    }

    @Override
    public Boolean syncCodeMeta(Long id) {
        Code code = this.findById(id);
        List<CodeMeta> codeMetas = codeMetaService.findByCodeId(code.getId());
        List<CodeMeta> dbTableColumns = codeMetaService.findColumnsByTableName(code.getTableName(), code.getDsName());
        return syncColumn(id, codeMetas, dbTableColumns);
    }

    @Override
    @Transactional
    public Boolean syncColumn(Long id, List<CodeMeta> codeMetas, List<CodeMeta> dbTableColumns) {
        if (CollectionUtils.isEmpty(dbTableColumns)) {
            throw new RuntimeException("同步数据失败，原表结构不存在");
        }

        List<String> tableColumnNames = codeMetas.stream().map(CodeMeta::getColumnName).collect(Collectors.toList());

        List<String> dbTableColumnNames = dbTableColumns.stream().map(CodeMeta::getColumnName).collect(Collectors.toList());

        dbTableColumns.forEach(column -> {
            if (!tableColumnNames.contains(column.getColumnName())) {
                column.setCodeId(id);
                CodeGenUtils.initColumnField(column);
                codeMetaService.save(column);
            }
        });

        List<CodeMeta> delColumns = codeMetas.stream().filter(column -> !dbTableColumnNames.contains(column.getColumnName())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(delColumns)) {
            List<Long> ids = delColumns.stream().map(CodeMeta::getId).collect(Collectors.toList());
            codeMetaService.removeByIds(ids);
        }

        return true;
    }

    private Code prepareCodeMeta(Long id) {
        Code code = findById(id);
        List<CodeMeta> codeMetas = codeMetaService.findByCodeId(code.getId());
        code.setCodeMetaList(codeMetas);

        if (code.getTemplate().equals(GenConstants.TPL_ONE_TO_MANY) && code.getSubTableId() != null) {
            Code subCode = findById(code.getSubTableId());
//            subCode.setCodeMetaList(codeMetaService.findByCodeId(subCode.getId()));
            code.setSubCode(subCode);
        }

        code.setRelationCode(codeMapper.selectList(Wrappers.<Code>query().lambda().eq(Code::getSubTableId, code.getId())));
        return code;
    }
}
