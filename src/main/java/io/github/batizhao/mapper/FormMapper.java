package io.github.batizhao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.batizhao.domain.Form;
import org.apache.ibatis.annotations.Mapper;

/**
 * 表单
 *
 * @author batizhao
 * @since 2021-03-08
 */
@Mapper
public interface FormMapper extends BaseMapper<Form> {

}
