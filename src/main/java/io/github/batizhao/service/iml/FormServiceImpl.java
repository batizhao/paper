package io.github.batizhao.service.iml;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.batizhao.exception.NotFoundException;
import io.github.batizhao.domain.Form;
import io.github.batizhao.mapper.FormMapper;
import io.github.batizhao.service.FormService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 表单接口实现类
 *
 * @author batizhao
 * @since 2021-03-08
 */
@Service
public class FormServiceImpl extends ServiceImpl<FormMapper, Form> implements FormService {

    @Autowired
    private FormMapper formMapper;

    @Override
    public IPage<Form> findForms(Page<Form> page, Form form) {
        LambdaQueryWrapper<Form> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(form.getName())) {
            wrapper.like(Form::getName, form.getName());
        }
        return formMapper.selectPage(page, wrapper);
    }

    @Override
    public Form findById(Long id) {
        Form form = formMapper.selectById(id);

        if(form == null) {
            throw new NotFoundException(String.format("没有该记录 '%s'。", id));
        }

        return form;
    }

    @Override
    @Transactional
    public Form saveOrUpdateForm(Form form) {
        if (form.getId() == null) {
            form.setCreateTime(LocalDateTime.now());
            form.setUpdateTime(LocalDateTime.now());
            formMapper.insert(form);
        } else {
            form.setUpdateTime(LocalDateTime.now());
            formMapper.updateById(form);
        }

        return form;
    }

    @Override
    @Transactional
    public Boolean updateStatus(Form form) {
        LambdaUpdateWrapper<Form> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Form::getId, form.getId()).set(Form::getStatus, form.getStatus());
        return formMapper.update(null, wrapper) == 1;
    }

}
