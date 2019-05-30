package xunshan.spring.post_processor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PostProcessorConfig.class)
public class PostProcessorConfigTest {
	@Autowired
	private Bar bar;

	@Test
	public void beanPostProcessor_BeanFacotryAwareCalled() {
		assertNotNull(bar);
		assertNotNull(bar.getBf());
		assertSame(bar, bar.getBf().getBean("bar"));
	}
}