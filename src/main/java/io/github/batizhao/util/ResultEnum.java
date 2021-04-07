package io.github.batizhao.util;

/**
 * 定义返回码的枚举类
 *
 * 返回消息码：两位应用编码（统一编排，通常对应 boot 应用，10-99） + 两位模块编码（00为通用错误） + 两位错误编码（01-99）
 * 如当前应用编码为10，发生了"缺少请求体"的错误，这是一个通用错误（00），所以是 1000，再加上错误编码（01），最后是 100001
 * 系统级错误 10
 *
 * @author batizhao
 * @since 2020-02-20
 */
public enum ResultEnum {

    /**
     * 成功
     */
    SUCCESS(0, "ok"),

    /**
     * 系统错误
     */
    UNKNOWN_ERROR(100000, "出错了！"),
    PARAMETER_INVALID(100001, "参数不合法！"),
    OAUTH2_TOKEN_ERROR(100002, "获取访问令牌失败！"),
    OAUTH2_TOKEN_INVALID(100003, "访问令牌不合法！"),
    PERMISSION_UNAUTHORIZED_ERROR(100004, "认证失败！"),
    PERMISSION_FORBIDDEN_ERROR(100005, "权限不足！"),
    GATEWAY_ERROR(10008, "网关异常！"),
    TOO_MANY_REQUEST(10009, "您的请求被限流了！"),
    RESOURCE_NOT_FOUND(100010, "没有找到相关资源！"),
    MQ_MESSAGE_ERROR(100011, "系统内部错误，请联系管理员！"),

    /**
     * IMS 01 模块错误
     */
    IMS_USER_NOT_FOUND(100100, "用户不存在！"),

    /**
     * SYSTEM 02 模块错误
     */
    SYSTEM_STORAGE_ERROR(100200, "存储异常！"),

    /**
     * DP 03 模块错误
     */
    DP_DS_ERROR(100300, "数据源参数异常！");

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
