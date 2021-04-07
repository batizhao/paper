package io.github.batizhao.service.iml;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.batizhao.exception.NotFoundException;
import io.github.batizhao.domain.DictType;
import io.github.batizhao.mapper.DictTypeMapper;
import io.github.batizhao.service.DictTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 字典类型接口实现类
 *
 * @author batizhao
 * @since 2021-02-07
 */
@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements DictTypeService {

    @Autowired
    private DictTypeMapper dictTypeMapper;

    @Override
    public IPage<DictType> findDictTypes(Page<DictType> page, DictType dictType) {
        LambdaQueryWrapper<DictType> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(dictType.getName())) {
            wrapper.like(DictType::getName, dictType.getName());
        }
        return dictTypeMapper.selectPage(page, wrapper);
    }

    @Override
    public DictType findById(Long id) {
        DictType dictType = dictTypeMapper.selectById(id);

        if (dictType == null) {
            throw new NotFoundException(String.format("没有该记录 '%s'。", id));
        }

        return dictType;
    }

    @Override
    @Transactional
    public DictType saveOrUpdateDictType(DictType dictType) {
        if (dictType.getId() == null) {
            dictType.setCreateTime(LocalDateTime.now());
            dictType.setUpdateTime(LocalDateTime.now());
            dictTypeMapper.insert(dictType);
        } else {
            dictType.setUpdateTime(LocalDateTime.now());
            dictTypeMapper.updateById(dictType);
        }

        return dictType;
    }

    @Override
    @Transactional
    public Boolean updateStatus(DictType dictType) {
        LambdaUpdateWrapper<DictType> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(DictType::getId, dictType.getId()).set(DictType::getStatus, dictType.getStatus());
        return dictTypeMapper.update(null, wrapper) == 1;
    }
}
