package me.batizhao.ims.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "权限类")
public class Permission {

    @ApiModelProperty(value = "权限ID", example = "100")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "权限URL", example = "/user/common")
    @NotBlank(message = "url is not blank")
    private String url;

    @ApiModelProperty(value = "权限名", example = "common")
    @NotBlank(message = "name is not blank")
    private String name;

    @ApiModelProperty(value = "权限说明", example = "This is admin permission")
    private String description;

    @ApiModelProperty(value = "权限父ID", example = "100")
    private Long pid;
}
