<!---
question: Spring MVC的工作原理
tags: 
 - Spring
 - MVC
--->

# Spring MVC的工作原理

## DispatcherServlet as FrontController

- **将HTTP请求映射到指定的处理方法**
- 将请求报文转化成DTO对象
- 生成响应，如json等

DispatcherServlet核心逻辑：

```
// 找到对应的Handler
mappedHandler = getHandler(processedRequest);
// 创建合适的适配器，可能有多种输出形式，如json，xml
HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
// 调用处理方法
mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
// 输出返回，由HandlerMethodReturnValueHandler实现
```

## HandlerMapping mapping request to handler

```
@GetMapping("hi")
public String hi(String name) {
    return "hi " + name;
}
```

`hi`方法的抽象是`HandlerMethod`。HandlerMapping通过url`hi`，查找到对应的`HandlerMethod`。默认有两个实现：

- RequestMappingHandlerMapping 
- BeanNameUrlHandlerMapping

## HandlerAdapter coordinate request, response and handler

本例中用这个RequestMappingHandlerAdapter。HandlerAdapter实现了高度可拓展性，通过`supports`这个方法来指定可以控制的
handler类型。大多数核心逻辑在这里实现。

## ResponseBodyResultHandler handling result

在这个例子中，返回带有@ResponseBody标记，直接由ResponseBodyResultHandler处理，并不是走ModelAndView。Spring 5引进的响应式架构
也是这么处理的。

最后执行这一下：

1. 参数解析：HandlerMethodArgumentResolver，这里则是对`name`的解析
2. 执行InvocableHandlerMethod#doInvoke方法
3. 处理返回：RequestResponseBodyMethodProcessor#handleReturnValue，即返回String

## 使用的模式

- MVC模式
- Adapter模式
- 代理模式：见HandlerMethodReturnValueHandlerComposite
- FrontController

## 代码清单

### Controller

```
@RestController
public class Controller {
	@GetMapping("hi")
	public String hi(@RequestParam("name") String name) {
		return "hi " + name;
	}
}
```

### Tomcat & DispatcherServlet

Tomcat使用了嵌入式Tomcat：

```
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>${spring-base}</version>
    </dependency>

    <dependency>
        <groupId>org.apache.tomcat.embed</groupId>
        <artifactId>tomcat-embed-core</artifactId>
        <version>8.5.32</version>
    </dependency>
</dependencies>
```

```
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
```