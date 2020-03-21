package me.batizhao.ims.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "角色")
public class RoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色ID", example = "100")
    private Long id;

    /**
     * @mock ROLE_@string("upper", 3, 20)
     */
    @ApiModelProperty(value = "角色名", example = "管理员")
    @NotBlank
    private String name;

}
