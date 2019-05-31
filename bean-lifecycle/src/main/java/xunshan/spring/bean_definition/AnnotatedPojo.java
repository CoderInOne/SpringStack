package xunshan.spring.bean_definition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
@Lazy
public class AnnotatedPojo {
	private String a = "hi";

	@Autowired
	private Foo foo;

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}
}
