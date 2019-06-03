package xunshan.spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Aspect
public class LogAspect {
	@Pointcut("execution(public void *(..))")
	private void targetMethod() {
	}

	@Around("targetMethod()")
	public Object doLogOnTargetMethod(ProceedingJoinPoint pjp) throws Throwable {
		if (pjp == null) {
			return null;
		}

		System.out.printf("[%s] [%s] [%s] [%s]\n",
				"Thread:" + Thread.currentThread().getName(),
				new Date(System.currentTimeMillis()).toString(),
				pjp,
				"before");

		Object result = pjp.proceed();

		System.out.printf("%s %s %s %s\n", new Date(System.currentTimeMillis()).toString()
				, pjp, Thread.currentThread().getName(), "finished");

		return result;
	}
}
