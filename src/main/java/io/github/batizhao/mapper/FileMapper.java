package io.github.batizhao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.batizhao.domain.File;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author batizhao
 * @date 2020/9/23
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {
}
