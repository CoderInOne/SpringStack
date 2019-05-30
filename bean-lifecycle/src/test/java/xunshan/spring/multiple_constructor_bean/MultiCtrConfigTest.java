package xunshan.spring.multiple_constructor_bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MultiCtrConfig.class)
public class MultiCtrConfigTest {
	@Autowired
	private BeanMulpitpleCtr bean;

	@Test
	public void beanInject() {
		assertNotNull(bean);
	}
}