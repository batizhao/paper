package me.batizhao.ims.api.feign.factory;

import feign.hystrix.FallbackFactory;
import me.batizhao.ims.api.feign.UserFeignService;
import me.batizhao.ims.api.feign.fallback.UserServiceFallbackImpl;
import org.springframework.stereotype.Component;

/**
 * @author batizhao
 * @since 2020-03-13
 **/
@Component
public class UserServiceFallbackFactory implements FallbackFactory<UserFeignService> {

	@Override
	public UserFeignService create(Throwable throwable) {
		UserServiceFallbackImpl userServiceFallback = new UserServiceFallbackImpl();
		userServiceFallback.setCause(throwable);
		return userServiceFallback;
	}
}
