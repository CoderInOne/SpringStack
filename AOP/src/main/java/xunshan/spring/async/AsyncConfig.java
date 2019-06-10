package xunshan.spring.async;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@ComponentScan
@EnableAsync
//@EnableAspectJAutoProxy
public class AsyncConfig /*implements AsyncConfigurer*/ {
	/*@Bean
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
	}*/
}
