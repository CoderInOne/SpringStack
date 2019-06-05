package xunshan.spring.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncOperation {
	public AsyncOperation() {
		System.out.println("init async operation");
	}

	@Async
	public void doAsyncAction(Runnable r) {
		System.out.println(Thread.currentThread());
		if (r != null) {
			r.run();
		}
	}

	@MyAsync
	public void doMyAsyncAction(Runnable r) {
		System.out.println(Thread.currentThread());
		if (r != null) {
			r.run();
		}
	}
}
