package xunshan.spring.benchmark;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BenchmarkConfig.class)
public class BenchmarkConfigTest {
	@Autowired
	private TestMethod testMethod;

	@Test
	public void aop() {
		long s = System.nanoTime();
		for (int i = 0; i < 1000000; i++) {
			testMethod.log();
		}
		System.out.println("   aop:" + (System.nanoTime() - s));
	}

	@Test
	public void noAop() {
		long s = System.nanoTime();
		for (int i = 0; i < 1000000; i++) {
			testMethod.logNoAop();
		}
		System.out.println("no aop:" + (System.nanoTime() - s));
	}
}