package xunshan.spring.bean_definition;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AnnotatedCtxConfig.class)
public class AnnotatedCtxConfigTest {
	@Autowired
	private GenericApplicationContext appCtx;

	@Test
	public void beanReader_registerBeanDefByAnnotatedClass() {
		AnnotatedBeanDefinitionReader bdfReader =
				new AnnotatedBeanDefinitionReader(appCtx.getDefaultListableBeanFactory());
		assertNotNull(bdfReader);
		bdfReader.registerBean(AnnotatedPojo.class, "pojo");

		// check bean definition whether match annotation metadata
		BeanDefinition bdf = bdfReader.getRegistry().getBeanDefinition("pojo");
		assertNotNull(bdf);
		assertEquals("xunshan.spring.bean_definition.AnnotatedPojo",
				     bdf.getBeanClassName());
		assertEquals("prototype", bdf.getScope());
		assertTrue(bdf.isLazyInit());
	}
}