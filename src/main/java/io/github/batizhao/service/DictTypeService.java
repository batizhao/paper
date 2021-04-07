package io.github.batizhao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.batizhao.domain.DictType;

/**
 * 字典类型接口类
 *
 * @author batizhao
 * @since 2021-02-07
 */
public interface DictTypeService extends IService<DictType> {

    /**
     * 分页查询
     * @param page 分页对象
     * @param dictType 字典类型
     * @return IPage<DictType>
     */
    IPage<DictType> findDictTypes(Page<DictType> page, DictType dictType);

    /**
     * 通过id查询字典类型
     * @param id id
     * @return DictType
     */
    DictType findById(Long id);

    /**
     * 添加或修改字典类型
     * @param dictType 字典类型
     * @return DictType
     */
    DictType saveOrUpdateDictType(DictType dictType);

    /**
     * 更新字典类型状态
     * @param dictType 字典类型
     * @return Boolean
     */
    Boolean updateStatus(DictType dictType);
}
