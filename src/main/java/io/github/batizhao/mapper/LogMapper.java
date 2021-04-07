package io.github.batizhao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.batizhao.domain.Log;
import org.apache.ibatis.annotations.Mapper;

/**
 * 日志
 *
 * @author batizhao
 * @since 2021-03-01
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {

}
