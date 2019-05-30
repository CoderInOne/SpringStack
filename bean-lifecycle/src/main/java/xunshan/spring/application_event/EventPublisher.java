package xunshan.spring.application_event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {
	@Autowired
	private ApplicationEventPublisher publisher;

	public void pulish(String message) {
		publisher.publishEvent(new SimpleEvent(this, message));
	}
}
