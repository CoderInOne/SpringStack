package xunshan.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class MyDispatcherServlet extends DispatcherServlet {
	private static final long serialVersionUID = -3314754572000698529L;

	public void refresh(ApplicationContext context) {
		super.onRefresh(context);
	}
}
