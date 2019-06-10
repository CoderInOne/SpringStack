package xunshan.spring.aop_proxy;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MyMethodInterceptor implements MethodInterceptor {
	public Object invoke(MethodInvocation invocation) throws Throwable {
		System.out.println("MyMethodInterceptor invoke:" + invocation.getMethod());
		return invocation.getMethod().invoke(invocation.getThis(), invocation.getArguments());
	}
}
