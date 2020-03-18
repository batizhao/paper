package io.github.batizhao.uaa.feign;

import io.github.batizhao.common.core.constant.SecurityConstants;
import io.github.batizhao.common.core.util.ResponseInfo;
import io.github.batizhao.ims.core.vo.RoleVO;
import io.github.batizhao.ims.core.vo.UserVO;
import io.github.batizhao.uaa.feign.factory.UserServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-13
 **/
@FeignClient(value = "ims", fallbackFactory = UserServiceFallbackFactory.class)
public interface UserFeignService {

    @GetMapping(value = "user/username")
    ResponseInfo<UserVO> getByUsername(@RequestParam("username") String username,
                                       @RequestHeader(SecurityConstants.FROM) String from);

    @GetMapping(value = "role")
    ResponseInfo<List<RoleVO>> getRolesByUserId(@RequestParam("userId") Long userId,
                                                @RequestHeader(SecurityConstants.FROM) String from);

}
