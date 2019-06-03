package xunshan.spring.bean_injection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xunshan.spring.bean_instantiation.Foo;

@Component
public class ConstructorInjection {
	private Foo foo;

	@Autowired
	public ConstructorInjection(Foo foo) {
		this.foo = foo;
	}

	public ConstructorInjection() {
		System.out.println("init ConstructorInjection");
	}

	public Foo getFoo() {
		return foo;
	}
}
