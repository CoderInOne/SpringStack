package xunshan.spring;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Aspect
public class LogAspect {
	@Pointcut("within(xunshan.spring.*)")
	void executionPointCut() { }

	@Pointcut("within(xunshan.spring.*)")
	void onWithinBlock() { }

	@Around("executionPointCut()")
	public Object doLogOnTargetMethod(ProceedingJoinPoint pjp) throws Throwable {
		// 方法前输出日志
		System.out.printf("[%s] [%s]\n", pjp, "before");

		// 执行真正的逻辑
		Object result = pjp.proceed();

		// 方法后输出日志
		System.out.printf("[%s] [%s]\n", pjp, "finished");

		return result;
	}

	@AfterThrowing(value = "executionPointCut()", throwing = "e")
	public void doOnExceptionThrowing(JoinPoint pjp, Throwable e) {
		System.out.println("doOnExceptionThrowing");

		// swallow exception for test
		// e.printStackTrace();
	}
}
