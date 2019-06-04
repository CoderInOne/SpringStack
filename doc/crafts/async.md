`@Async`标记一个方法为异步执行，线程池与异常处理器则在`AsyncConfigurer`中配置。使用方法：

```
@Configuration
@ComponentScan
@EnableAsync
@EnableAspectJAutoProxy
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

// 测试代码
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

笔者在粗略跟踪了源代码后，DIY了一个@MyAsync注解：

```
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAsync {
}

@Aspect
@Component
public class MyAsyncAspect {
    // 沿用上面提供的线程池
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
					throwable.printStackTrace();
				}
			}
		});

		return null;
	}
}

// 测试代码
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