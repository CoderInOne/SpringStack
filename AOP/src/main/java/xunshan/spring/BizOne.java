package xunshan.spring;

import org.springframework.stereotype.Component;

@Component
public class BizOne {
	@Log
	public void doOneAction() {
		System.out.println("do action one");
	}

	public void doThrowEx() {
		throw new RuntimeException("ops");
	}
}
