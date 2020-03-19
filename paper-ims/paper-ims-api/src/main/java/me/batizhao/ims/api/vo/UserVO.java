package me.batizhao.ims.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-14
 **/
@Data
@Accessors(chain = true)
@ApiModel(description = "用户")
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID", example = "100")
    private Long id;

    /**
     * @mock @word(3,30)
     */
    @ApiModelProperty(value = "用户名", example = "zhangsan")
    @NotBlank
    private String username;

    @ApiModelProperty(value = "密码")
    @NotBlank
    private String password;

    @ApiModelProperty(value = "邮箱", example = "zhangsan@qq.com")
    @NotBlank
    @Email
    private String email;

    /**
     * @mock @cname
     */
    @ApiModelProperty(value = "姓名", example = "张三")
    @NotBlank
    private String name;

    /**
     * @mock @datetime
     */
    @ApiModelProperty(value = "创建时间")
    private Date time;

    /**
     * 角色列表
     */
    @ApiModelProperty(value = "角色列表")
    private List<RoleVO> roleList;
}
