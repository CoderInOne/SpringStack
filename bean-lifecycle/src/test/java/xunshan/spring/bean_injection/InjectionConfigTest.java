package xunshan.spring.bean_injection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = InjectionConfig.class)
public class InjectionConfigTest {
	@Autowired
	private SetterInjection injectToObject;
	@Autowired
	private ConstructorInjection constructorInjection;

	@Test
	public void injectBySetter() {
		assertNotNull(injectToObject.getFoo());
	}

	@Test
	public void injectByConstructor() {
		assertNotNull(constructorInjection.getFoo());
	}
}