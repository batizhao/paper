package io.github.batizhao.exception;

/**
 * 定义返回码的枚举类
 *
 * @author batizhao
 * @since 2020-02-20
 */
public enum ResultEnum {

    /**
     * 成功
     */
    SUCCESS(1000, "成功"),
    /**
     * 未知错误
     */
    UNKNOWN_ERROR(1001, "未知的错误!"),
    /**
     * 无法找到资源
     */
    NOT_FOUNT_RESOURCE(1002, "没有找到相关资源!"),
    /**
     * 请求参数有误
     */
    PARAMETER_ERROR(1003, "请求参数有误!"),
    /**
     * 确少必要请求参数异常
     */
    PARAMETER_MISSING_ERROR(1004, "确少必要请求参数!"),
    /**
     * 确少必要请求参数异常
     */
    REQUEST_MISSING_BODY_ERROR(1005, "缺少请求体!");

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
