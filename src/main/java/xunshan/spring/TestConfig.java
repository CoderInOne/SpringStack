package xunshan.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
	class Pojo {
		String a = "a";
	}

	@Bean
	Pojo pojo() {
		return new Pojo();
	}
}
