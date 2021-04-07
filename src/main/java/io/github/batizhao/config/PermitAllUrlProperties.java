package io.github.batizhao.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;

/**
 * @author batizhao
 * @since 2020-03-20
 *
 * <p>
 * 不鉴权的接口配置
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "security.oauth2.ignore")
public class PermitAllUrlProperties implements InitializingBean, ApplicationContextAware {

	private Ant ant = new Ant();
	private Regex regex = new Regex();
	private ApplicationContext applicationContext;
	private StringBuilder sb = new StringBuilder();

	@Override
	public void afterPropertiesSet() {
		log.info("ant urls path: {}", ant.urls);
		log.info("regex urls path: {}", regex.urls);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 支持 ant 表达式
	 */
	@Data
	public static class Ant {

		private List<String> urls = new ArrayList<>();

	}

	/**
	 * 支持 regex 表达式
	 */
	@Data
	public static class Regex {

		private List<String> urls = new ArrayList<>();

	}
}
