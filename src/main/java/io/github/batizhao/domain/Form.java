package io.github.batizhao.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 表单 实体对象
 *
 * @author batizhao
 * @since 2021-03-08
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "表单")
@TableName("form")
public class Form implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 表单名称
     */
    @ApiModelProperty(value="表单名称")
    private String name;

    /**
     * 表单元数据
     */
    @ApiModelProperty(value="表单元数据")
    private String metadata;

    /**
     * 表单描述
     */
    @ApiModelProperty(value="表单描述")
    private String description;

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

    
}
