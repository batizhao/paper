package io.github.batizhao.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 生成代码元数据表
 *
 * @author batizhao
 * @since 2021-02-01
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "生成代码元数据表")
public class CodeMeta extends Model<CodeMeta> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value="pk")
    private Long id;

    /**
     * code表ID
     */
    @ApiModelProperty(value="code表ID")
    private Long codeId;

    /**
     * 列名
     */
    @ApiModelProperty(value="列名")
    private String columnName;

    /**
     * 列注释
     */
    @ApiModelProperty(value="列注释")
    private String columnComment;

    /**
     * 列类型
     */
    @ApiModelProperty(value="列类型")
    private String columnType;

    /**
     * Java类型
     */
    @ApiModelProperty(value="Java类型")
    private String javaType;

    /**
     * Java属性名
     */
    @ApiModelProperty(value="Java属性名")
    private String javaField;

    /**
     * 是否主键
     */
    @ApiModelProperty(value="是否主键")
    private Boolean primaryKey;

    /**
     * 是否自增
     */
    @ApiModelProperty(value="是否自增")
    private Boolean increment;

    /**
     * 是否必须
     */
    @ApiModelProperty(value="是否必须")
    private Boolean required;

    /**
     * 是否可插入
     */
    @ApiModelProperty(value="是否可插入")
    private Boolean save;

    /**
     * 是否可编辑
     */
    @ApiModelProperty(value="是否可编辑")
    private Boolean edit;

    /**
     * 是否在列表显示
     */
    @ApiModelProperty(value="是否在列表显示")
    private Boolean display;

    /**
     * 是否可查询
     */
    @ApiModelProperty(value="是否可查询")
    private Boolean search;

    /**
     * 查询方式（等于、不等于、大于、小于、范围）
     */
    @ApiModelProperty(value="查询方式（等于、不等于、大于、小于、范围）")
    private String searchType;

    /**
     * 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    @ApiModelProperty(value="显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）")
    private String htmlType;

    /**
     * 字典类型
     */
    @ApiModelProperty(value="字典类型")
    private String dictType;

    /**
     * 排序
     */
    @ApiModelProperty(value="排序")
    private Integer sort;

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
