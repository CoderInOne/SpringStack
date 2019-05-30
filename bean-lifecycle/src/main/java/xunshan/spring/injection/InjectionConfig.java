package xunshan.spring.injection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import xunshan.spring.bean_instantiation.Foo;

@Configuration
@ComponentScan
public class InjectionConfig {
	@Bean
	public Foo foo() {
		return new Foo();
	}
}
