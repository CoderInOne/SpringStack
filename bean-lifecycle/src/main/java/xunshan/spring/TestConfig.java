package xunshan.spring;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import xunshan.spring.post_processor.BeanFactoryAwareProcessor;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan
public class TestConfig {
	class Pojo {
		String a = "a";
	}

	@Bean
	BeanPostProcessor beanFactoryAwareProcessor(ConfigurableApplicationContext ctx) {
		return new BeanFactoryAwareProcessor(ctx);
	}

	@PostConstruct
	public void init() {
		System.out.println("post init");
	}

	@Bean
	Pojo pojo() {
		return new Pojo();
	}
}
