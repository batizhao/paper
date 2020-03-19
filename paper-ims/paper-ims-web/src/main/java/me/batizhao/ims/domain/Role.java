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
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "角色")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色ID", example = "100")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * @mock ROLE_@string("upper", 3, 20)
     */
    @ApiModelProperty(value = "角色名", example = "管理员")
    @NotBlank(message = "name is not blank")
    @Size(min = 3, max = 30)
    private String name;

}
