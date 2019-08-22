package xunshan.spring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	@GetMapping("hi")
	public String hi(@RequestParam("name") String name) {
		return "hi " + name;
	}

	@GetMapping("hi/1")
	public String hiOne(@RequestParam("name") String name) {
		return "hi " + name;
	}
}
