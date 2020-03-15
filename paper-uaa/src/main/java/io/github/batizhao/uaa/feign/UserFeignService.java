package io.github.batizhao.uaa.feign;

import io.github.batizhao.common.core.util.ResponseInfo;
import io.github.batizhao.ims.core.vo.RoleVO;
import io.github.batizhao.ims.core.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-13
 **/
@FeignClient(value = "ims")
public interface UserFeignService {

    @GetMapping(value = "user/username")
    ResponseInfo<UserVO> getByUsername(@RequestParam("username") String username);

    @GetMapping(value = "role")
    ResponseInfo<List<RoleVO>> getRolesByUserId(@RequestParam("userId") Long userId);

}
