package io.github.batizhao.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author batizhao
 * @since 2020-04-01
 **/
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "日志")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日志ID", example = "100")
    private Long id;

    @ApiModelProperty(value = "HTTP方法", example = "GET")
    @NotBlank(message = "httpRequestMethod is not blank")
    private String httpRequestMethod;

    @ApiModelProperty(value = "类名", example = "me.batizhao.ims.web.RoleController")
    @NotBlank(message = "className is not blank")
    private String className;

    @ApiModelProperty(value = "类方法", example = "findRolesByUserId")
    @NotBlank(message = "classMethod is not blank")
    private String classMethod;

    @ApiModelProperty(value = "操作描述", example = "删除用户")
    @NotBlank(message = "description is not blank")
    @Size(min = 1, max = 100)
    private String description;

    @ApiModelProperty(value = "操作参数")
    private String parameter;

    @ApiModelProperty(value = "日志类型")
    private String type;

    @ApiModelProperty(value = "操作结果")
    private String result;

    @ApiModelProperty(value = "操作时长", example = "100")
    private Integer spend;

    @ApiModelProperty(value = "OAuth客户端", example = "client_app")
    private String clientId;

    @ApiModelProperty(value = "操作用户", example = "admin")
    @NotBlank(message = "username is not blank")
    @Size(min = 3, max = 30)
    private String username;

    @ApiModelProperty(value = "操作URL", example = "http://192.168.1.1/xxx")
    @NotBlank(message = "url is not blank")
    private String url;

    /**
     * @mock @ip
     */
    @ApiModelProperty(value = "操作IP", example = "192.168.1.1")
    @NotBlank(message = "ip is not blank")
    private String ip;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
