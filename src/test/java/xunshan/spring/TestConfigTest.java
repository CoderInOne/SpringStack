package xunshan.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class TestConfigTest {
	@Autowired
	private TestConfig.Pojo pojo;

	@Test
	public void pojo() {
		assertNotNull(pojo);
		assertEquals("a", pojo.a);
	}
}