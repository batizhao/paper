package me.batizhao.ims.api.feign;

import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.common.core.util.ResponseInfo;
import me.batizhao.ims.api.vo.RoleVO;
import me.batizhao.ims.api.vo.UserVO;
import me.batizhao.ims.api.feign.factory.UserServiceFallbackFactory;
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

    @GetMapping(value = "/user/userdetail")
    ResponseInfo<UserVO> loadUserByUsername(@RequestParam("username") String username,
                                            @RequestHeader(SecurityConstants.FROM) String from);

//    @GetMapping(value = "role")
//    ResponseInfo<List<RoleVO>> getRolesByUserId(@RequestParam("userId") Long userId,
//                                                @RequestHeader(SecurityConstants.FROM) String from);

}
