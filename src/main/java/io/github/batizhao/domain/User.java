package io.github.batizhao.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "用户")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID", example = "100")
    private Long id;

    @ApiModelProperty(value = "用户名", example = "zhangsan")
    @NotBlank(message = "username is not blank")
    @Size(min = 3, max = 30)
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "邮箱", example = "zhangsan@qq.com")
    @NotBlank(message = "email is not blank")
    @Email
    private String email;

    /**
     * @mock @cname
     */
    @ApiModelProperty(value = "姓名", example = "张三")
    @NotBlank(message = "name is not blank")
    private String name;

    /**
     * 用户性别
     */
    @ApiModelProperty(value="用户性别")
    private String sex;

    /**
     * 手机号码
     */
    @ApiModelProperty(value="手机号码")
    private String mobileNumber;

    /**
     * @mock @url
     */
    @ApiModelProperty(value = "用户头像", example = "https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png")
    private String avatar;

    @ApiModelProperty(value = "未读消息数量", example = "99")
    private Integer unreadCount;

    /**
     * 状态
     */
    @ApiModelProperty(value="状态")
    private String status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;

//    /**
//     * 角色列表
//     */
//    @ApiModelProperty(value = "角色列表")
//    private transient List<Role> roleList;
}
