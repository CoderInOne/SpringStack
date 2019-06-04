package xunshan.spring.async;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Aspect
@Component
public class MyAsyncAspect {
	@Autowired
	private Executor asyncExecutor;

	@Pointcut("@annotation(xunshan.spring.async.MyAsync)")
	private void doMyAsyncCall() { }

	@Around("doMyAsyncCall()")
	public Object doMyAsyncCallIntercept(final ProceedingJoinPoint pjp) {
		System.out.println("call pc");
		asyncExecutor.execute(new Runnable() {
			public void run() {
				try {
					pjp.proceed();
				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}
			}
		});

		return null;
	}
}
