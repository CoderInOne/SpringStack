package xunshan.spring.application_event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class SimpleEventListener implements ApplicationListener<SimpleEvent> {
	private CountDownLatch latch = new CountDownLatch(1);
	private SimpleEvent event;

	SimpleEvent getEvent() throws InterruptedException {
		latch.await();
		return this.event;
	}

	public void onApplicationEvent(SimpleEvent event) {
		this.event = event;
		latch.countDown();
	}
}
