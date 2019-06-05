package xunshan.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@RestController
public class Controller {
	@GetMapping
	public String hi(String name) {
		return "hi " + name;
	}
}
