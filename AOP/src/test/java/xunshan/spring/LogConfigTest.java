package xunshan.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LogConfig.class)
public class LogConfigTest {
	@Autowired
	private LogAspect logAspect;
	@Autowired
	private BizOne bizOne;
	@Autowired
	private BizTwo bizTwo;

	@Test
	public void logAop() throws Throwable {
		LogAspect l = Mockito.spy(logAspect);

		bizOne.doOneAction();
		bizTwo.doTwoAction();

		// verify(l.doLogOnTargetMethod(any(ProceedingJoinPoint.class)), times(2));
	}
}