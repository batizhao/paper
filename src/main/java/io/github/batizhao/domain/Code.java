package io.github.batizhao.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 生成代码
 *
 * @author batizhao
 * @since 2021-01-28
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "生成代码")
public class Code extends Model<Code> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 数据源
     */
    @ApiModelProperty(value="数据源")
    private String dsName;

    /**
     * 表名
     */
    @ApiModelProperty(value="表名")
    private String tableName;

    /**
     * 表说明
     */
    @ApiModelProperty(value="表说明")
    private String tableComment;

    /**
     * 引擎
     */
    @ApiModelProperty(value="引擎")
    private String engine;

    /**
     * 类名
     */
    @ApiModelProperty(value="类名")
    private String className;

    /**
     * 类注释
     */
    @ApiModelProperty(value="类注释")
    private String classComment;

    /**
     * 作者
     */
    @ApiModelProperty(value="作者")
    private String classAuthor;

    /**
     * 包名
     */
    @ApiModelProperty(value="包名")
    private String packageName;

    /**
     * 所属模块/微服务/系统名，英文
     */
    @ApiModelProperty(value="所属模块/微服务/系统名，英文")
    private String moduleName;

    /**
     * API后端路由
     */
    @ApiModelProperty(value="API后端路由")
    private String mappingPath;

    /**
     * 模板类型
     */
    @ApiModelProperty(value="模板类型")
    private String template;

    /**
     * 父菜单ID
     */
    @ApiModelProperty(value="父菜单ID")
    private Long parentMenuId;

    /**
     * 生成代码方式（zip压缩包 path自定义路径）
     */
    @ApiModelProperty(value="生成代码方式（zip压缩包 path自定义路径）")
    private String type;

    /**
     * 生成路径（不填默认项目路径）
     */
    @ApiModelProperty(value="生成路径（不填默认项目路径）")
    private String path;

    /**
     * 关联子表的code.id
     */
    @ApiModelProperty(value="关联子表的code.id")
    private Long subTableId;

    /**
     * 子表关联的属性名
     */
    @ApiModelProperty(value="子表关联的属性名")
    private String subTableFkName;

    /**
     * 表单元数据
     */
    @ApiModelProperty(value="表单元数据")
    private String options;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;

    /**
     * 表元数据
     */
    @ApiModelProperty(value="表元数据")
    private transient List<CodeMeta> codeMetaList;

    /**
     * 子表元数据
     */
    @ApiModelProperty(value="子表元数据")
    private transient Code subCode;

    /**
     * 关联表元数据
     */
    @ApiModelProperty(value="关联表元数据")
    private transient List<Code> relationCode;

}
