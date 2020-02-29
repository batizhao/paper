package io.github.batizhao.domain;

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
@ApiModel(description = "角色类")
public class Role {

    @ApiModelProperty(value = "角色ID", example = "100")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "角色名", example = "管理员")
    @NotBlank(message = "name is not blank")
    private String name;

}
