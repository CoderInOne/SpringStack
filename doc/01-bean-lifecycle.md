# Bean的生命周期

Bean，对象也。Spring作为一个依赖注入框架，那么Bean是如何创建的，如何销毁的呢？

首先，我们有个类：Pojo，现在我要让Spring帮我创建这个类，Spring有两种办法定义Bean：

- XML
- 注解

以注解为例，简单看看使用的流程：

```
// 定义类
class Pojo {
    String a = "a";
}

// 定义Bean
@Configuration
public class TestConfig {
	@Bean
	Pojo pojo() {
		return new Pojo();
	}
}

// 使用Bean
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class TestConfigTest {
	@Autowired
	private Pojo pojo;

	@Test
	public void pojo() {
		assertNotNull(pojo);
		assertEquals("a", pojo.a);
	}
}
```

不妨在`new Pojo()`的一行加个断点，因为这种方式，必然会走这一行创建Pojo

我们首先看到：

`org.springframework.test.context.support.AbstractGenericContextLoader.loadContext(java.lang.String...)`

所谓上下文，可以理解为master，这个master无所不知，以至于要知道Spring的某个东西，首先要经过它。这里ContextLoader，不难想到，
Spring有XML和注解方式，那么Loader就是用来读取配置，并创建上下文的。大家可以先看下loadContext的注释，一开始云里雾里，没有关系，
咋们继续往下走。

```
<li>Calls {@link #validateMergedContextConfiguration(MergedContextConfiguration)}
	 * to allow subclasses to validate the supplied configuration before proceeding.</li>
	 * <li>Creates a {@link GenericApplicationContext} instance.</li>
	 * <li>If the supplied {@code MergedContextConfiguration} references a
	 * {@linkplain MergedContextConfiguration#getParent() parent configuration},
	 * the corresponding {@link MergedContextConfiguration#getParentApplicationContext()
	 * ApplicationContext} will be retrieved and
	 * {@linkplain GenericApplicationContext#setParent(ApplicationContext) set as the parent}
	 * for the context created by this method.</li>
	 * <li>Calls {@link #prepareContext(GenericApplicationContext)} for backwards
	 * compatibility with the {@link org.springframework.test.context.ContextLoader
	 * ContextLoader} SPI.</li>
	 * <li>Calls {@link #prepareContext(ConfigurableApplicationContext, MergedContextConfiguration)}
	 * to allow for customizing the context before bean definitions are loaded.</li>
	 * <li>Calls {@link #customizeBeanFactory(DefaultListableBeanFactory)} to allow for customizing the
	 * context's {@code DefaultListableBeanFactory}.</li>
	 * <li>Delegates to {@link #loadBeanDefinitions(GenericApplicationContext, MergedContextConfiguration)}
	 * to populate the context from the locations or classes in the supplied
	 * {@code MergedContextConfiguration}.</li>
	 * <li>Delegates to {@link AnnotationConfigUtils} for
	 * {@link AnnotationConfigUtils#registerAnnotationConfigProcessors registering}
	 * annotation configuration processors.</li>
	 * <li>Calls {@link #customizeContext(GenericApplicationContext)} to allow for customizing the context
	 * before it is refreshed.</li>
	 * <li>Calls {@link #customizeContext(ConfigurableApplicationContext, MergedContextConfiguration)} to
	 * allow for customizing the context before it is refreshed.</li>
	 * <li>{@link ConfigurableApplicationContext#refresh Refreshes} the
	 * context and registers a JVM shutdown hook for it.</li>
```

接下来，我们看到`org.springframework.context.ConfigurableApplicationContext.refresh`这个"传奇"方法，哈哈

```
// 首先创建Bean

// 准备：校验validateRequiredProperties
prepareRefresh

// 回调子类刷新，把当前的各类依赖注册到子类的BeanFactory中
refreshBeanFactory

// 这里的一大段主要是初始化Context和BeanFactory
// Allows post-processing of the bean factory in context subclasses.
postProcessBeanFactory(beanFactory);
// Invoke factory processors registered as beans in the context.
invokeBeanFactoryPostProcessors(beanFactory);
// Register bean processors that intercept bean creation.
registerBeanPostProcessors(beanFactory);
// Initialize message source for this context.
initMessageSource();
// Initialize event multicaster for this context.
initApplicationEventMulticaster();
// Initialize other special beans in specific context subclasses.
onRefresh();
// Check for listener beans and register them.
registerListeners();
// Instantiate all remaining (non-lazy-init) singletons.
finishBeanFactoryInitialization(beanFactory);
// Last step: publish corresponding event.
finishRefresh();

// 开始创建bean了
~ConfigurableListableBeanFactory.preInstantiateSingletons
// 创建定义好的bean
DefaultListableBeanFactory.beanDefinitionNames

for (String beanName : beanNames) {
    RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
    if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit()) {
        // 如果是工厂Bean，则创建之
        if (isFactoryBean(beanName)) {
            Object bean = getBean(FACTORY_BEAN_PREFIX + beanName);
            if (bean instanceof FactoryBean) {
                final FactoryBean<?> factory = (FactoryBean<?>) bean;
                boolean isEagerInit;
                if (System.getSecurityManager() != null && factory instanceof SmartFactoryBean) {
                    isEagerInit = AccessController.doPrivileged((PrivilegedAction<Boolean>)
                                    ((SmartFactoryBean<?>) factory)::isEagerInit,
                            getAccessControlContext());
                }
                else {
                    isEagerInit = (factory instanceof SmartFactoryBean &&
                            ((SmartFactoryBean<?>) factory).isEagerInit());
                }
                if (isEagerInit) {
                    getBean(beanName);
                }
            }
        }
        else {
            // 创建
            getBean(beanName);
        }
    }
}

// Bean的实际创建流程
AbstractBeanFactory#createBean -> doCreateBean -> createBeanInstance
AbstractAutowireCapableBeanFactory#instantiateUsingFactoryMethod
// 创建的方式有几种：构造器，工厂方法
// 构造器可能有很多个：~ConstructorResolver

// 策略器调用示例化
~SimpleInstantiationStrategy#instantiate
// 最后我们看到的是CGLIB的东西
~BeanMethodInterceptor
```

梳理下，我们看到涉及的组件：

- ApplicationContext Bean容器，根据元数据创建，配置，链接Bean
- BeanFactory        访问容器的基本接口，ApplicationContext是其子类
- BeanDefinitionRegistry Bean定义容器
- Bean/BeanDefinition
- FactoryBean        Bean的创建工厂，如TestConfig
- ConstructorResolver 构造器解析器
- InstantiationStrategy 构造策略
- CGLIB: BeanMethodInterceptor
- Scope 有单例，Prototype之分

## BeanDefinition的加载与注册

首先元数据要么定义在注解里，要么定义在XML中。在这里，我们玩点搞怪的东西，假设我这两种方式我都不喜欢，我只想用YAML的方式来定义Bean。
实现的思路：

- 继承ApplicationContext的某个子类，通过XML注册方式找到：AbstractRefreshableConfigApplicationContext，因为ApplicationContext在
BeanFactory基础上实现资源访问
- 解析文件，注册到BeanFactory中

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
		bdf.setBeanClassName("xunshan.spring.Foo");
		bdf.setFactoryBeanName(null);
		bdf.getPropertyValues().add("a", "foo");
		list.add(bdf);

		bdf = new GenericBeanDefinition();
		bdf.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
		bdf.setBeanClassName("xunshan.spring.TestConfig");
		list.add(bdf);

		return list;
	}
}

// 测试代码
YmlApplicationContext appCtx = new YmlApplicationContext();
appCtx.refresh();
Foo foo = (Foo) appCtx.getBean("xunshan.spring.Foo");
assertNotNull(foo);
assertEquals("foo", foo.foo());
```

看起来也不太难嘛！理解这个Bean注册过程会大有用处，这以为着可以实现在项目启动的时候，将我想要的Bean预先注册进来，比如
我们的项目中，就实现了启动时批量注册加载数据源。大多数中间件整合进Spring的时候都会采用这种方式哦，比如motan就采用类似的实现。

BeanDefinition文档：https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html#beans-definition

备注疑点：

```
org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(org.springframework.beans.factory.support.RootBeanDefinition, java.lang.String, org.springframework.beans.factory.BeanFactory, java.lang.Object, java.lang.reflect.Method, java.lang.Object...)
这里为什么会用线程局部呢
答案在此：intercept:361, ConfigurationClassEnhancer$BeanMethodInterceptor

ConstrutorResolver 419行，为什么要取FactoryBean，即TestConfig的所有方法呢？candidates是候选，最终会选举一个factoryMethodToUse，为什么
不是直接取呢？
```