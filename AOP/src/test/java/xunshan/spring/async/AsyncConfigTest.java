package xunshan.spring.async;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AsyncConfig.class)
public class AsyncConfigTest {
//	@Autowired
//	private AsyncOperation asyncOperation;

	@Test
	public void asyncOperation() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		final String curThreadName = Thread.currentThread().getName();
		AsyncOperation asyncOperation = new AsyncOperation();
		asyncOperation.doAsyncAction(new Runnable() {
			public void run() {
				// 当前线程不是main线程，说明doAsyncAction方法是异步执行
				assertNotEquals(curThreadName, Thread.currentThread().getName());

				latch.countDown();
			}
		});
		latch.await();
	}

	/*@Test
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
	}*/
}