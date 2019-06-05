package xunshan.spring;

import org.apache.catalina.Context;
import org.apache.catalina.Server;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.io.File;

//@SpringBootApplication
public class MVCApplication  {
//	public static void main(String[] args) {
//		SpringApplication.run(MVCApplication.class, args);
//	}

	public static void main(String[] args) throws Exception {
		Tomcat tomcat = new Tomcat();

		tomcat.setPort(7080);
		tomcat.setHostname("localhost");
		String appBase = ".";
		tomcat.getHost().setAppBase(appBase);

		File docBase = new File(System.getProperty("java.io.tmpdir"));
		Context context = tomcat.addContext("", docBase.getAbsolutePath());

		addServlet(context);
		// addFilter(context);

		Server server = tomcat.getServer();

		tomcat.start();
		server.await();
	}

//	private static void addFilter(Context context) {
//		Class filterClass = HelloFilter.class;
//		FilterDef def = new FilterDef();
//		def.setFilterClass(filterClass.getName());
//		def.setFilterName(filterClass.getSimpleName());
//		context.addFilterDef(def);
//
//		FilterMap fmap = new FilterMap();
//		fmap.setFilterName(filterClass.getSimpleName());
//		fmap.addURLPattern("/hello/*");
//		context.addFilterMap(fmap);
//	}

	private static void addServlet(Context context) {
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(Controller.class);
		ctx.refresh();

		MyDispatcherServlet servlet = new MyDispatcherServlet();
		servlet.setApplicationContext(ctx);
		servlet.refresh(ctx);

		Tomcat.addServlet(context, "dispatcher-servlet", servlet);
		context.addServletMappingDecoded("/hello/*", "dispatcher-servlet");
	}
}
