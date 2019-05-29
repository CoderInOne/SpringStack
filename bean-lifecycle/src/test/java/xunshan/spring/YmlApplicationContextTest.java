package xunshan.spring;

import org.junit.Test;
import static org.junit.Assert.*;

public class YmlApplicationContextTest {
	@Test
	public void registerBdfAndGetBeanSuccess() {
		YmlApplicationContext appCtx = new YmlApplicationContext();
		appCtx.refresh();
		Foo foo = (Foo) appCtx.getBean("xunshan.spring.Foo");
		assertNotNull(foo);
		assertEquals("foo", foo.foo());
	}
}