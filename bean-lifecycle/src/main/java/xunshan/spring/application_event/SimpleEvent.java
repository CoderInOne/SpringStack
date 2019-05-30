package xunshan.spring.application_event;

import org.springframework.context.ApplicationEvent;

public class SimpleEvent extends ApplicationEvent {
	private final String message;
	/**
	 * Create a new ApplicationEvent.
	 *
	 * @param source the object on which the event initially occurred (never {@code null})
	 */
	public SimpleEvent(Object source, String message) {
		super(source);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
