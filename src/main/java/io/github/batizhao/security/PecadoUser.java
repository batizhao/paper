package io.github.batizhao.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class PecadoUser extends User {

    /**
     * 用户ID
     */
    @Getter
    private Long userId;

    /**
     * 部门ID
     */
    @Getter
    private Long deptId;

    public PecadoUser(Long userId, Long deptId, String username, String password, boolean enabled,
                   boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                   Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.deptId = deptId;
    }

}
