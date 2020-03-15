package io.github.batizhao.ims.core.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Data
@Accessors(chain = true)
public class RoleVO {

    private Long id;

    private String name;

}
