# Bean定义加载与注册

Bean定义的加载与注册是Bean加载的第一道工序。这里以注解方式为例，主要讲一下几个方面：

- 总体流程
- 读取Bean定义
- 注册Bean定义

## 总体流程

先看一段简单的代码：

```
@Configuration
public class TestConfig {
	public class Pojo {
		public String a = "a";
	}

	@Bean
	Pojo pojo() {
		return new Pojo();
	}

	public static void main(String[] args) {
		ApplicationContext appCtx =
				new AnnotationConfigApplicationContext(TestConfig.class);
		Pojo pojo = (Pojo) appCtx.getBean("pojo");
		System.out.println(pojo.a);
	}
}
```
跟踪ApplicationContext的初始化，总结的主要步骤如下：

```
// 实例化注解式Bean定义阅读器，如果是包名则是扫描器
this.reader = new AnnotatedBeanDefinitionReader(this);
this.scanner = new ClassPathBeanDefinitionScanner(this);

// 读取Bean定义，并注册Bean
this.reader.register(annotatedClasses);

// 刷新上下文：创建对象，注入
refresh();
```

## Bean定义的解析

下面的测试用例用来研究下Bean定义的读取。测试用例中分别检查了类名，是否单例，是否懒加载。
元数据都保存在注解和注解的目标中。

```
@Scope("prototype")
@Lazy
public class AnnotatedPojo {
	private String a = "hi";

	@Autowired
	private Foo foo;

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}
}

public class Foo { }

@Configuration
public class AnnotatedCtxConfig { }

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AnnotatedCtxConfig.class)
public class AnnotatedCtxConfigTest {
	@Autowired
	private GenericApplicationContext appCtx;

	@Test
	public void beanReader_registerBeanDefByAnnotatedClass() {
		AnnotatedBeanDefinitionReader bdfReader =
				new AnnotatedBeanDefinitionReader(appCtx.getDefaultListableBeanFactory());
		assertNotNull(bdfReader);
		bdfReader.registerBean(AnnotatedPojo.class, "pojo");

		// check bean definition whether match annotation metadata
		BeanDefinition bdf = bdfReader.getRegistry().getBeanDefinition("pojo");
		assertNotNull(bdf);
		assertEquals("xunshan.spring.bean_definition.AnnotatedPojo",
				     bdf.getBeanClassName());
		assertEquals("prototype", bdf.getScope());
		assertTrue(bdf.isLazyInit());
	}
}
```

我们不难理解，注解方式的Bean定义读取无非是通过class的反射机制和注解信息得来，这个机制作为一个联系留给大家。
这里给出对应表：

[](images/bean_def_source.png)

## Bean定义注册

第一个问题：注册的大体流程是怎样的？这个问题只需要debug下就可以解决了，下面是debug的堆栈

```
// 注册
registerBeanDefinition:791, DefaultListableBeanFactory
registerBeanDefinition:321, GenericApplicationContext
registerBeanDefinition:149, BeanDefinitionReaderUtils

// 读取Bean
doRegisterBean:246, AnnotatedBeanDefinitionReader
registerBean:145, AnnotatedBeanDefinitionReader
register:135, AnnotatedBeanDefinitionReader
register:158, AnnotationConfigApplicationContext
```

第二个问题，存放Bean定义的数据结构是什么？答案很容易：`Map`，而且是个ConcurrentHashMap。理解这两个问题，

## 自定义Bean定义加载与注册

假设，XML和注解定义Bean方式，我都不喜欢，我只想用YAML的方式来定义Bean。怎么实现呢？首先我们回顾下核心流程，得出伪代码：

1. 加载Bean定义，文件或注解
2. 构建对应的BeanDefinition对象
3. 获取BeanDefinitionRegistry对象，调用注册方法

下面是代码示例：

```
public class YmlApplicationContext extends AbstractRefreshableConfigApplicationContext {
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
		for (BeanDefinition bd : getBeanDefinistions()) {
			beanFactory.registerBeanDefinition(bd.getBeanClassName(), bd);
		}
	}

	// mock load bean from yml file
	List<BeanDefinition> getBeanDefinistions() {
		List<BeanDefinition> list = new ArrayList<BeanDefinition>();

		GenericBeanDefinition bdf = new GenericBeanDefinition();
		bdf.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
		bdf.setBeanClass(Foo.class);
		bdf.setBeanClassName("xunshan.spring.bean_instantiation.Foo");
		bdf.setFactoryBeanName(null);
		bdf.getPropertyValues().add("a", "foo");
		list.add(bdf);

		bdf = new GenericBeanDefinition();
		bdf.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
		bdf.setBeanClassName("xunshan.spring.bean_instantiation.TestConfig");
		list.add(bdf);

		return list;
	}
}

// 测试代码
YmlApplicationContext appCtx = new YmlApplicationContext();
appCtx.refresh();
Foo foo = (Foo) appCtx.getBean("xunshan.spring.bean_instantiation.Foo");
assertNotNull(foo);
assertEquals("foo", foo.foo());
```

看起来也不太难嘛！理解这个Bean注册过程会大有用处，这意味着可以实现在项目启动的时候，将我想要的Bean预先注册进来，比如
我们的项目中，就实现了启动时批量注册加载数据源。大多数中间件整合进Spring的时候都会采用这种方式哦，比如motan就采用类似的实现。

## 总结

Bean定义的加载与注册的核心流程：

1. 加载Bean定义，文件或注解
2. 构建对应的BeanDefinition对象
3. 获取BeanDefinitionRegistry对象，调用注册方法

推荐阅读源代码列表：

- AnnotationConfigApplicationContext的构造方法，核心流程就是在这里实现的
- AnnotatedBeanDefinitionReader.doRegisterBean(...)，实现注解和类信息的读取，BeanDefinition的实例化和注册
- GenericApplicationContext#registerBeanDefinition(...)，实现Bean定义的注册，Bean定义相关数据结构在此
