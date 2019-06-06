package xunshan.spring;

import org.apache.catalina.Context;
import org.apache.catalina.Server;
import org.apache.catalina.startup.Tomcat;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;
import java.io.File;

public class MVCApplication {
	public static void main(String[] args) throws Exception {
		Tomcat tomcat = new Tomcat();

		tomcat.setPort(7080);
		tomcat.setHostname("localhost");
		String appBase = ".";
		tomcat.getHost().setAppBase(appBase);

		File docBase = new File(System.getProperty("java.io.tmpdir"));
		Context context = tomcat.addContext("", docBase.getAbsolutePath());

		addServlet(context);

		Server server = tomcat.getServer();

		tomcat.init();
		server.start();
		server.await();
	}

	private static void addServlet(Context context) throws ServletException {
		DispatcherServlet servlet = prepareDispatcherServlet();

		Tomcat.ExistingStandardWrapper wrapper = (Tomcat.ExistingStandardWrapper)
				Tomcat.addServlet(context, "dispatcher-servlet", servlet);
		context.addServletMappingDecoded("/hello/*", "dispatcher-servlet");
		// 预加载
		wrapper.loadServlet();
	}

	private static DispatcherServlet prepareDispatcherServlet() {
		// 启动上下文
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(Controller.class);
		ctx.refresh();

		DispatcherServlet servlet = new DispatcherServlet();
		servlet.setApplicationContext(ctx);
		// onApplicationEvent会调用refresh，同时告诉FrameServlet上下文刷洗完毕，避免重复刷新的问题
		servlet.onApplicationEvent(new ContextRefreshedEvent(ctx));
		return servlet;
	}
}
