package me.batizhao.common.core.util;

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
    RESOURCE_NOT_FOUND(100010, "没有找到相关资源！"),

    /**
     * 用户模块错误
     */
    USER_NOT_FOUND(100100, "用户不存在！");

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
