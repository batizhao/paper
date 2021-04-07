package io.github.batizhao.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典 实体对象
 *
 * @author batizhao
 * @since 2021-02-08
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "字典")
public class DictData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 代码
     */
    @ApiModelProperty(value="代码")
    private String code;

    /**
     * 标签
     */
    @ApiModelProperty(value="标签")
    private String label;

    /**
     * 值
     */
    @ApiModelProperty(value="值")
    private String value;

    /**
     * 是否默认
     */
    @ApiModelProperty(value="是否默认")
    private String isDefault;

    /**
     * 排序
     */
    @ApiModelProperty(value="排序")
    private Long sort;

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
