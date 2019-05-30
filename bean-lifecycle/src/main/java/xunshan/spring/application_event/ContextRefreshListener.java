package xunshan.spring.application_event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {
	private ApplicationEvent event;

	public void onApplicationEvent(ContextRefreshedEvent event) {
		this.event = event;
	}

	public ApplicationEvent getEvent() {
		return event;
	}
}
