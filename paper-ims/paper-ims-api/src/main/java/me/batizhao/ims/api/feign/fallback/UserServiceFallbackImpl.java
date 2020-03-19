package me.batizhao.ims.api.feign.fallback;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.ims.api.feign.UserFeignService;
import me.batizhao.ims.api.vo.UserVO;
import org.springframework.stereotype.Component;

/**
 * @author batizhao
 * @since 2020-03-13
 **/
@Slf4j
@Component
public class UserServiceFallbackImpl implements UserFeignService {

    @Setter
    private Throwable cause;

    @Override
    public ResponseInfo<UserVO> loadUserByUsername(String username, String from) {
        log.error("feign 查询用户信息失败: {}", username, cause);
        return null;
    }

//    @Override
//    public ResponseInfo<List<RoleVO>> getRolesByUserId(Long userId, String from) {
//        log.error("feign 查询用户角色信息失败: {}", userId, cause);
//        return null;
//    }
}
