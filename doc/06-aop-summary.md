<!--
question: AOP原理与引用
tags: Spring, AOP
-->

# Spring面向切面编程

面向过程的基本单元是一个函数，面向对象的基本单元是一个类。而面向切面则是对一组逻辑进行管理，比如对一组方法调用进行监控，日志输出，事务等。这种需求似乎
用面向对象的方式也可以实现，比如定义一个抽象方法，把日志从业务逻辑中分离出来。那如果要求对原有的代码不做任何改动呢？这时候面向过程和面向对象就
无能为力了。那如果我们做如是想：类似于正则匹配的方式，**原有代码**中只要满足或者需要输出日志的地方，都需要输出下日志。不对原有代码改动，并不意味
这执行代码不许改动，也就是说我们可以在byte code级别进行改动，在符合要求的执行代码前后"植入"日志代码，然后重新打包，这样不就能实现**不改动源代码
的情况下，统一对某个行为进行增强**。所以，这个思路可以总结如下：

```
1. for each method should be logged
2.     plugin in logging byte code before method
3. re-package
```

接下来，我们以事务为例，一步一步讲明白AOP核心概念，这里先列个提纲：

- 切面（Aspect）
- 增强（Advice）
- 切点（Pointcut）
- 连接点（JointPoint）

使用的知识点：

- byte code

## 示例：输出方法调用日志

定义两个类，要求对方法的执行前后进行日志的输出：

```
@Component
public class BizOne {
	@Log
	public void doOneAction() {
		System.out.println("do action one");
	}

	public void doThrowEx() {
		throw new RuntimeException("ops");
	}
}

@Component
public class BizTwo {
	public void doTwoAction() {
		System.out.println("action two");
	}
}
```

这里涉及到有个概念叫：JointPoint，连接点，一般指的是一个方法的执行，这里是`doOneAction`和`BizTwo`这两个方法。
把方法抽象成一个点，方便在上面展开逻辑，这种方法和在物理上点的抽象有异曲同工之妙。

我们看下如下表述：

- 仅对`doOneAction`方法输出日志

因为方法有很多，怎么样只对`doOneAction`方法实现日志输出呢？我们很快想到正则匹配。这种正则匹配在AOP中叫做：Pointcut。意思是说
符合正则表达式的地方切下一个口，来执行我的日志逻辑。实现如下：

```
@Component
@Aspect
public class LogAspect {
	@Around("execution(* xunshan.spring.BizOne.*()") // <- PointCut
	public Object doLogOnTargetMethod(ProceedingJoinPoint pjp  // <- JointPoint
	    ) throws Throwable {
		// 方法前输出日志
		System.out.printf("[%s] [%s]\n", pjp, "before");

		// 执行真正的逻辑
		Object result = pjp.proceed();

		// 方法后输出日志
		System.out.printf("[%s] [%s]\n", pjp, "finished");

		return result;
	}
}
```

PointCut: `execution(* xunshan.spring.BizOne.*(..))`解析

- execution代表匹配方法的JoinPoint，也就是方法的拦截
- `public void xunshan.spring.BizOne.*()`表示BizOne中的任何方法

对PointCut之前执行，之后执行又是怎么定义的呢？首先看@Around，表示在这个方法前后执行某个操作，这种叫做Advice，在上面的例子中Advice的操作
就是`doLogOnTargetMethod`。Advice一般有：

- Before: 在JoinPoint之前
- After： 在JoinPoint之后
- Around：在JoinPoint前后
- AfterThrowing: 在JoinPoint抛异常之后

接下来，我想在排除异常的时候也加进去：

```
@AfterThrowing(value = "execution(* xunshan.spring.BizOne.*())", throwing = "e")
public void doOnExceptionThrowing(JoinPoint pjp, Throwable e) {
    System.out.println("doOnExceptionThrowing");
}
```

由于`doLogOnTargetMethod`和`doOnExceptionThrowing`这两个PointCut是同一个，不妨把他们放到一起：

```
@Component
@Aspect
public class LogAspect {
	@Pointcut("execution(* xunshan.spring.BizOne.*())")
	void executionMethodInBizOne() { }

	@Around("executionMethodInBizOne()")
	public Object doLogOnTargetMethod(ProceedingJoinPoint pjp) throws Throwable {
		...
	}

	@AfterThrowing(value = "executionMethodInBizOne()", throwing = "e")
	public void doOnExceptionThrowing(JoinPoint pjp, Throwable e) {
		...
	}
}
```

这里`doLogOnTargetMethod`和`doOnExceptionThrowing`两个Advice组成的组件叫做Aspect。综上所述，AOP的编程步骤可以归纳为：

1. 确定关注点，如日志输出
2. 定义PointCut表达式
3. 确定Advice，如Around，Before等
4. 整理成Aspect

## Spring AOP原理

AOP主要应用是方法，以方法为例。要对一个方法，进行灵活有效的操纵。没有比代理模式更加适用了。代理模式可参考[proxy@iluwater][1]。
Spring AOP的代理是CGLib实现的动态代理，其实现见所以如果我们要实现对方法的拦截：

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
这里需要注意的是Advise以CGLib的callback方式注册，可能多于一个。CGLib的callback类似于JDK代理的InvokeHandler，
比起InvokeHandler更容易使用，InvokeHandler处理不当会造成死循环。

## CGLib vs JDK proxy

JDK:

- 只代理接口，可代理多个接口
- 所有方法代理到InvocationHandler接口
- 性能：所有方法代理到统一接口，需要进一步转发，有性能的损耗，toString/clone这类方法，通常不需要改写

CGLib:

- 以子类形式代理，不支持final方法和类
- 性能：指定对某个方法注入byte code，减少方法转发

方法转发的意思说每个方法有个index，调用的时候用这个index去virtual method table查找实现。

[1]: https://github.com/iluwatar/java-design-patterns/tree/master/proxy
[2]: https://dzone.com/articles/cglib-missing-manual