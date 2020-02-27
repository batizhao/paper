package io.github.batizhao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 尽量不要在这里写 @MapperScan
 * 因为会导致单元测试的时候初始化 Mapper，造成 Service 和 Controller 的一些错误
 * 必须用 @AutoConfigureMybatis 解决这个问题，但在环境中，又会造成其它的问题
 * 所以，这里都直接在 Mapper 上采用 @Mapper 的方法实现
 *
 *  @author batizhao
 *  @since 2020-02-07
 */
@SpringBootApplication
public class PaperApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaperApplication.class, args);
	}
}
