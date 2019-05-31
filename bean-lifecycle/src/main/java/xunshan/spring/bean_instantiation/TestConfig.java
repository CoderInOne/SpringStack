package xunshan.spring.bean_instantiation;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import xunshan.spring.post_processor.BeanFactoryAwareProcessor;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan
public class TestConfig {
	public class Pojo {
		public String a = "a";
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

	public static void main(String[] args) {
		AnnotationConfigApplicationContext appCtx =
				new AnnotationConfigApplicationContext(TestConfig.class);
		Pojo pojo = (Pojo) appCtx.getBean("pojo");
		System.out.println(pojo.a);
	}
}
