package xunshan.spring.bean_definition;

import org.junit.Test;
import xunshan.spring.bean_definition.YmlApplicationContext;
import xunshan.spring.bean_instantiation.Foo;

import static org.junit.Assert.*;

public class YmlApplicationContextTest {
	@Test
	public void registerBdfAndGetBeanSuccess() {
		YmlApplicationContext appCtx = new YmlApplicationContext();
		appCtx.refresh();
		Foo foo = (Foo) appCtx.getBean("xunshan.spring.bean_instantiation.Foo");
		assertNotNull(foo);
		assertEquals("foo", foo.foo());
	}
}