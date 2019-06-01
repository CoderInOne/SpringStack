package xunshan.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xunshan.spring.bean_instantiation.Bar;
import xunshan.spring.bean_instantiation.FooObjectFactory;
import xunshan.spring.bean_instantiation.Pojo;
import xunshan.spring.bean_instantiation.TestConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class TestConfigTest {
	@Autowired
	private Pojo pojo;

	@Autowired
	private Bar bar;

	@Autowired
	private FooObjectFactory fooObjectFactory;

	@Test
	public void createPojoByFactoryMethod() {
		assertNotNull(pojo);
		assertEquals("a", pojo.a);
	}

	@Test
	public void createFooByObjectFactory() {
		assertNotNull(fooObjectFactory);
		assertNotNull(fooObjectFactory.getObject());
	}

	@Test
	public void createBarByConstructor() {
		assertNotNull(bar);
	}
}