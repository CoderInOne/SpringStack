package xunshan.spring.application_event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SimpleEventConfig.class)
public class SimpleEventConfigTest {
	@Autowired
	private SimpleEventListener simpleEventListener;
	@Autowired
	private EventPublisher eventPublisher;
	@Autowired
	private ContextRefreshListener contextRefreshListener;

	@Test
	public void publishAndReceiveApplicationEvent_Success() throws InterruptedException {
		final String message = "hi";
		Executors.newSingleThreadExecutor().submit(new Callable<Void>() {
			public Void call() throws Exception {
				Thread.sleep(3000);
				eventPublisher.pulish(message);
				return null;
			}
		});

		assertNotNull(simpleEventListener);
		assertNotNull(simpleEventListener.getEvent());
		assertEquals(simpleEventListener.getEvent().getMessage(), message);
	}

	@Test
	public void listenContextRefreshedEvent_Success() {
		assertNotNull(contextRefreshListener.getEvent());
	}
}