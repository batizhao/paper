package me.batizhao.ims.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class RolePermission {

    private Long roleId;

    private Long permissionId;

    private String url;
    private String roleName;

    public RolePermission(String url, String roleName) {
        this.url = url;
        this.roleName = roleName;
    }
}
