package io.github.batizhao.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import io.github.batizhao.util.TreeNode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "菜单")
public class Menu extends TreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    public Menu(Integer id, Integer pid) {
        this.id = id;
        this.pid = pid;
    }

    @ApiModelProperty(value = "路径", example = "/user/common")
    private String path;

    @ApiModelProperty(value = "菜单名", example = "权限管理")
    @NotBlank(message = "name is not blank")
    private String name;

    @ApiModelProperty(value = "权限名", example = "ims_root")
    private String permission;

    @ApiModelProperty(value = "权限说明", example = "This is admin permission")
    private String description;

    @ApiModelProperty(value = "图标", example = "icon-web")
    private String icon;

    @ApiModelProperty(value = "类型（L左侧 T顶部 B按钮）", example = "L")
    private String type;

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;

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

    /**
     * 路由元数据
     */
    @ApiModelProperty(value="路由元数据")
    private transient MetaVO meta;

}
