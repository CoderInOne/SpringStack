package xunshan.spring;

import org.springframework.stereotype.Component;

@Component
public class BizTwo {
	@Log
	public void doTwoAction() {
		System.out.println("action two");
	}
}
