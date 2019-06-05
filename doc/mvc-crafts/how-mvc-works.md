<!---
question: Spring MVC的工作原理
tags: 
 - Spring
 - MVC
--->

# Spring MVC的工作原理

在Boot模式下稍微麻烦一点。没有关系。从核心DispatcherServlet开始，其作用在于

- 将HTTP请求映射到指定的处理方法
- 将请求报文转化成DTO对象
- 生成响应，如json等

DispatcherServlet的继承体系：

```
GenericServlet (javax.servlet)
HttpServlet (javax.servlet.http)
HttpServletBean (org.springframework.web.servlet)
FrameworkServlet (org.springframework.web.servlet)
DispatcherServlet (org.springframework.web.servlet)
```

DispatcherServletAutoConfiguration这个类做了自动配置

注意EmbeddedTomcat的使用

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

MappingHandler: BeanNameUrlHandlerMapping/RequestMappingHandlerMapping
模式匹配的方式

HandlerAdapter: 
0 = {HttpRequestHandlerAdapter@2141} 
1 = {SimpleControllerHandlerAdapter@2165} 
2 = {RequestMappingHandlerAdapter@2616} <--

AbstractHandlerMethodAdapter: Controller里面的Handler本质上是HandlerMethod
HandlerAdapter核心功能：ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)

ViewResolver: InternalResourceViewResolver

```
ServletInvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
if (this.argumentResolvers != null) {
    // HandlerMethodArgumentResulverComposite
    invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
}
if (this.returnValueHandlers != null) {
    // HandlerMethodReturnValueHandlerComposite
    invocableMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
}
```

最后执行这一下：

1. 先Resolver argument出来
2. doInvoke
3. this.returnValueHandlers.handleReturnValue(returnValue, getReturnValueType(returnValue), mavContainer, webRequest);
4. 选择一个处理器HandlerMethodReturnValueHandlerComposite.selectHandler
5. RequestResponseBodyMethodProcessor#handleReturnValue -> @ResponseBody专用

ModelAndView

 * Records model and view related decisions made by
 * {@link HandlerMethodArgumentResolver}s and
 * {@link HandlerMethodReturnValueHandler}s during the course of invocation of
 * a controller method.
ModelAndViewContainer mavContainer = new ModelAndViewContainer();

```
InvocableHandlerMethod

@Nullable
public Object invokeForRequest(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer,
        Object... providedArgs) throws Exception {

    Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
    if (logger.isTraceEnabled()) {
        logger.trace("Invoking '" + ClassUtils.getQualifiedMethodName(getMethod(), getBeanType()) +
                "' with arguments " + Arrays.toString(args));
    }
    Object returnValue = doInvoke(args);
    if (logger.isTraceEnabled()) {
        logger.trace("Method [" + ClassUtils.getQualifiedMethodName(getMethod(), getBeanType()) +
                "] returned [" + returnValue + "]");
    }
    return returnValue;
}
```

这里加上Tomcat的请求处理，这个专题就算完整了