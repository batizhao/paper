package io.github.batizhao.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.batizhao.domain.Log;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
public interface LogService extends IService<Log> {

    /**
     * 分页查询日志
     * @param page 分页对象
     * @param log 日志
     * @return IPage<Log>
     */
    IPage<Log> findLogs(Page<Log> page, Log log);

    /**
     * 通过id查询日志
     * @param id id
     * @return Log
     */
    Log findById(Long id);

}
