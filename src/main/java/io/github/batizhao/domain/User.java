package io.github.batizhao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author batizhao
 * @since 2016/9/28
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户类")
public class User implements Serializable {

    @ApiModelProperty(value = "用户ID", example = "100")
    @TableId(type = IdType.AUTO)
    public Long id;

    @ApiModelProperty(value = "用户名", example = "zhangsan")
    @NotBlank(message = "username is not blank")
    @Size(min = 3, max = 30)
    private String username;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "password is not blank")
    private String password;

    @ApiModelProperty(value = "邮箱", example = "zhangsan@qq.com")
    @NotBlank(message = "email is not blank")
    @Email
    private String email;

    @ApiModelProperty(value = "姓名", example = "张三")
    @NotBlank(message = "name is not blank")
    private String name;

    @ApiModelProperty(value = "创建时间")
    private Date time;

//    @TableField(exist = false)
//    @ApiModelProperty(value = "权限")
//    private List<Role> authorities;
}
