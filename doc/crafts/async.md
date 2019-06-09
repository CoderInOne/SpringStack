# Spring里如何实现方法的异步执行

如题：在Spring里面如何让一个方法异步执行？答案是@Async注解，让一个方法异步执行的条件：

1. 开启异步执行：@EnableAsync
2. 配置线程池，非必须，没有则用默认线程池
3. Bean方法指定为异步：@Async

接下来分步骤详细讲解每一步

## 开启异步执行：@EnableAsync

只有开启@EnableAsync，打上@Async注解的方法才能异步执行

```
@Configuration
@EnableAsync
public class AsyncConfig {
    // ...
}
```

## 配置线程池

定制线程池需要实现`AsyncConfigurer`，并提供合适的线程池和异常处理器

```
@Configuration
@ComponentScan
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
	@Bean
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(4);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(11);
		executor.setThreadNamePrefix("MyExecutor-");
		executor.initialize();
		return executor;
	}

	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {
			public void handleUncaughtException(Throwable ex, Method method, Object... params) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		};
	}
}
```

## Bean方法指定为异步

注意异步方法所在的类必须是一个Bean，因为异步化通过AOP对方法拦截实现，不是Bean是无法做AOP操作的。

```
@Component
public class AsyncOperation {
	@Async
	public void doAsyncAction(Runnable r) {
		System.out.println(Thread.currentThread());
		if (r != null) {
			r.run();
		}
	}
}
```

##  测试代码

```
@Test
public void asyncOperation() throws InterruptedException {
	final CountDownLatch latch = new CountDownLatch(1);
	final String curThreadName = Thread.currentThread().getName();
	asyncOperation.doAsyncAction(new Runnable() {
		public void run() {
			// 异步执行断言
			assertNotEquals(curThreadName, Thread.currentThread().getName());
			
			latch.countDown();
		}
	});
	latch.await();
}
```

## 原理分析

笔者粗略跟踪源代码后，总结出异步执行的原理是AOP，具体步骤：

1. 初始化线程池和异常处理器
2. 创建异步方法所在Bean后，执行Async对应的BeanPostProcessor，创建AOP代理类，代理对象替换原来的对象
3. 代理对象中，异步方法被动态植入了异步执行方法
4. 执行异步方法，其实执行的是代理对象里面的方法，从而实现异步，除了Async这个注解，没有任何入侵

根据这个思路，笔者利用AOP机制，实现了一个简单的异步执行注解，功能和Async一样。实现分为以下部分：

- MyAsync注解，同Async注解
- MyAsyncAspect，实现方法拦截
- 一个线程池，细节不表

实现代码：

```
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAsync {
}

@Aspect
@Component
public class MyAsyncAspect {
    // 注入线程池
	@Autowired
	private Executor asyncExecutor;

	@Pointcut("@annotation(xunshan.spring.async.MyAsync)")
	private void doMyAsyncCall() { }

	@Around("doMyAsyncCall()")
	public Object doMyAsyncCallIntercept(final ProceedingJoinPoint pjp) {
		// 调用发生在线程池
		asyncExecutor.execute(new Runnable() {
			public void run() {
				try {
					pjp.proceed();
				} catch (Throwable throwable) {
					throw new RuntimeException(throwable);
				}
			}
		});

		return null;
	}
}

```

测试代码：

```
@Test
public void myAsyncOperation() throws InterruptedException {
    int jobs = 20;
    final CountDownLatch latch = new CountDownLatch(jobs);
    for (int i = 0; i < jobs; i++) {
        asyncOperation.doMyAsyncAction(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        });
    }

    latch.await();
}
```