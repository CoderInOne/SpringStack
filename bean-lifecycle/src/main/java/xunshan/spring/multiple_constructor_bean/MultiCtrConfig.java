package xunshan.spring.multiple_constructor_bean;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class MultiCtrConfig {
	@Bean
	@Qualifier("bar")
	Bar bar() {
		return new Bar();
	}

	@Bean
	@Qualifier("bar1")
	Bar bar1() {
		return new Bar();
	}
}
