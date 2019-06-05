package xunshan.spring;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.DefaultAopProxyFactory;
import xunshan.spring.aop_proxy.FooObject;
import xunshan.spring.aop_proxy.MyMethodInterceptor;

public class AopProxyTest {
	@Test
	public void createCglibBasedProxy() {
		DefaultAopProxyFactory apFactory = new DefaultAopProxyFactory();
		// AOP proxy configuration
		AdvisedSupport as = new AdvisedSupport(MethodInterceptor.class);

		// real object
		as.setTarget(new FooObject());
		// use CGLib proxy
		as.setOptimize(true);
		// add advice: method interceptor
		as.addAdvice(new MyMethodInterceptor());

		AopProxy aopProxy = apFactory.createAopProxy(as);
		FooObject proxy = (FooObject) aopProxy.getProxy();
		proxy.doAction();
	}
}
