package io.github.batizhao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.batizhao.domain.DictData;

/**
 * 字典接口类
 *
 * @author batizhao
 * @since 2021-02-08
 */
public interface DictDataService extends IService<DictData> {

    /**
     * 通过id查询字典
     * @param id id
     * @return DictData
     */
    DictData findById(Long id);

    /**
     * 添加或编辑字典
     * @param dictData 字典
     * @return DictData
     */
    DictData saveOrUpdateDictData(DictData dictData);

    /**
     * 更新字典状态
     * @param dictData 字典
     * @return Boolean
     */
    Boolean updateStatus(DictData dictData);
}
