package xunshan.spring.bean_instantiation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;

public class FooObjectFactory implements ObjectFactory {
	public Object getObject() throws BeansException {
		return new Foo();
	}
}
