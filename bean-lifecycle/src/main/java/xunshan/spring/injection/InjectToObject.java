package xunshan.spring.injection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xunshan.spring.bean_instantiation.Foo;

@Component
public class InjectToObject {
	private Foo foo;

	@Autowired
	public void setFoo(Foo foo) {
		this.foo = foo;
	}

	Foo getFoo() {
		return foo;
	}
}
