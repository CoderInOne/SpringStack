# AOP与代理模式

AOP主要应用是方法，以方法为例。要对一个方法，进行灵活有效的操纵。没有比代理模式更加适用了。代理模式可参考[proxy@iluwater][1]。
所以如果我们要实现对方法的拦截：

1. 在Bean创建后，调用代理创建BeanPostProcessor，目的是创建代理，见AbstractAutoProxyCreator
2. 注入代理对象
3. 调用代理方法，代理对象将调用转发给原对象

这里着重讲代理创建的过程，以一段代码为例：

```
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

// method interceptor advice
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class MyMethodInterceptor implements MethodInterceptor {
	public Object invoke(MethodInvocation invocation) throws Throwable {
		System.out.println("MyMethodInterceptor invoke:" + invocation.getMethod());
		return invocation.getMethod().invoke(invocation.getThis(), invocation.getArguments());
	}
}
```

增强方法的方式是CGLib，其本质是字节码改写，在此不做深入探究，可参考[cglib-missing-manual][2]
这里需要这一的是Advise以CGLib的callback方式注册，可能多于一个。CGLib的callback类似于JDK代理的InvokeHandler，
比起InvokeHandler更容易使用，InvokeHandler处理不当会造成死循环。

## CGLib vs JDK proxy

JDK:

- 只代理接口，可代理多个接口
- 所有方法代理到InvocationHandler接口
- 性能：所有方法代理到统一接口，需要进一步转发，有性能的损耗，toString/clone这类方法，通常不需要改写

CGLib:

- 以子类形式代理，不支持final方法和类
- 停止开发
- 性能：指定对某个方法注入byte code，减少方法转发

方法转发的意思说每个方法有个index，调用的时候用这个index去virtual method table查找实现。

[1]: https://github.com/iluwatar/java-design-patterns/tree/master/proxy
[2]: https://dzone.com/articles/cglib-missing-manual