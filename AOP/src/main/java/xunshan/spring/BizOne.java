package xunshan.spring;

import org.springframework.stereotype.Component;

@Component
public class BizOne {
	@Log
	public void doOneAction() {
		System.out.println("action one");
	}
}
