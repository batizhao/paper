package io.github.batizhao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.batizhao.domain.Form;

/**
 * 表单接口类
 *
 * @author batizhao
 * @since 2021-03-08
 */
public interface FormService extends IService<Form> {

    /**
     * 分页查询表单
     * @param page 分页对象
     * @param form 表单
     * @return IPage<Form>
     */
    IPage<Form> findForms(Page<Form> page, Form form);

    /**
     * 通过id查询表单
     * @param id id
     * @return Form
     */
    Form findById(Long id);

    /**
     * 添加或编辑表单
     * @param form 表单
     * @return Form
     */
    Form saveOrUpdateForm(Form form);

    /**
     * 更新表单状态
     * @param form 表单
     * @return Boolean
     */
    Boolean updateStatus(Form form);

}
