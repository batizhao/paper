package io.github.batizhao.exception;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 自定义返回实体类
 *
 * @author batizhao
 * @since 2020-02-20
 */
@Data
@Accessors(chain = true)
public class ResponseInfo<T> {

    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误信息
     */
    private String message = "";
    /**
     * 返回结果
     */
    private T data;

}
