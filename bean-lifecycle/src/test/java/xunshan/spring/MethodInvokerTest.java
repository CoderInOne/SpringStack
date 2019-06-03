package xunshan.spring;

import org.junit.Test;
import org.springframework.util.MethodInvoker;

public class MethodInvokerTest {
	@Test
	public void diff() {
		Class<?>[] classes = new Class[] { String.class, Integer.class };
		Object[] objects = new Object[] { "hi", "5" };

		System.out.println(MethodInvoker.getTypeDifferenceWeight(classes, objects));
	}
}
