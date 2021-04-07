package io.github.batizhao.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author batizhao
 * @date 2020/9/23
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "文件")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件ID", example = "100")
    private Long id;

    @ApiModelProperty(value = "显示名", example = "xxx")
    @NotBlank(message = "name is not blank")
    private String name;

    @ApiModelProperty(value = "文件名", example = "xxx.jpg")
    @NotBlank(message = "fileName is not blank")
    private String fileName;

    @ApiModelProperty(value = "大小", example = "99")
    private Long size;

    @ApiModelProperty(value = "url", example = "/path/xxx.jpg")
    private String url;

    @ApiModelProperty(value = "缩略图 url", example = "/path/xxx.jpg")
    private String thumbUrl;

    @ApiModelProperty(value = "操作时间")
    @NotNull(message = "createTime is not blank")
    private LocalDateTime createTime;

}
