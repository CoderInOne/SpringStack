package xunshan.spring.bean_instantiation;

import org.springframework.stereotype.Component;

// created by constructor
@Component
public class Bar {
	public Bar() {
		System.out.println("init bar");
	}
}
