package xunshan.spring.injection;

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
	private InjectToObject injectToObject;
	@Autowired
	private ConfigurableApplicationContext appCtx;

	@Test
	public void injectFooToObject_Success() {
		assertNotNull(injectToObject.getFoo());
	}

	@Test
	public void getInjectToObjectBeanDefinition() {
		BeanDefinition bdf = appCtx.getBeanFactory().getBeanDefinition("injectToObject");
		System.out.println(bdf);
		MutablePropertyValues pvs = bdf.getPropertyValues();
		System.out.println(pvs.getPropertyValueList());
	}
}