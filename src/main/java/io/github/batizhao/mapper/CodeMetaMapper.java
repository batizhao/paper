package io.github.batizhao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.batizhao.domain.CodeMeta;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 生成代码元数据表
 *
 * @author batizhao
 * @since 2021-02-01
 */
@Mapper
public interface CodeMetaMapper extends BaseMapper<CodeMeta> {

    /**
     * 查询表列信息
     * @param tableName 表名称
     * @return
     */
    List<CodeMeta> selectColumnsByTableName(@Param("tableName") String tableName);

}
