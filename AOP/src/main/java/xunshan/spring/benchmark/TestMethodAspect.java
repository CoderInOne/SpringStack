package xunshan.spring.benchmark;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TestMethodAspect {
	@Around("execution(* xunshan.spring.benchmark.TestMethod.log())")
	public Object doExeTestMethod(ProceedingJoinPoint pjp) throws Throwable {
		return pjp.proceed();
	}
}
