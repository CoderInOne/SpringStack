# Bean后置处理器

后置处理器是个hook函数，作用在于：

- 创建代理
- 修改创建的Bean

本节通过几个简单的例子，来讲讲后置处理器的使用。

## 日志采集

假设，我们要实现，Bean创建前后的日志输出，首先需要实现`BeanPostProcessor`，容器会将实现了这个接口的Bean，
注册到`BeanFactory`中，我们先看代码：

```
@Component
public class LoggerBeanPostProcessor implements BeanPostProcessor {

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("postProcessBeforeInitialization:" + 
				bean.getClass().getSimpleName() + ", " + beanName);
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("postProcessAfterInitialization:" + 
				bean.getClass().getSimpleName() + ", " + beanName);
		return bean;
	}
}
```

Bar示例创建的输出样例：

```
postProcessBeforeInitialization:Bar, bar
postProcessAfterInitialization:Bar, bar
```

我们看下大体的流程

1. 首先上下文刷新的时候，先创建和注册`BeanPostProcessor`，这一点很好理解，因为`BeanPostProcessor`会对Bean的创建
进行处理，所以要在Bean创建之前准备好
2. 为了确保所有的`BeanPostProcessor`都被注入进来，我们必须找到他的所有实现，这种功能通常由`ListableBeanFactory#getBeanNamesForType`
这组方法来实现，实现的方式是从所有的beanDefinitionNames中匹配，因为一开始我们就把所有的Bean定义加载进来了，这时候我们可以把Bean定义中的类型拿出来
和`BeanPostProcessor`匹配
3. 将2中匹配的后置处理器注册进`BeanFactory`中
4. 创建Bean完成后(AbstractAutowireCapableBeanFactory.doCreateBean)，遍历所有后置处理器，见AbstractAutowireCapableBeanFactory#
applyBeanPostProcessorsBeforeInitialization，进而调用postProcessBeforeInitialization方法。

## 实现容器核心组件注入

有时候，我们需要把一些核心组件注入进来直接使用，如`ApplicationContext`，Spring已经把这种需求抽象成一个接口了，见`ApplicationContextAware`。
那么，我们能不能实现类似这种功能的接口呢？假设，我对`BeanFactory`感兴趣，我想注入进来随时使用它。实现的思路是后置处理：给Bean附加一个Setter，这个
Setter在Bean创建成功后，由后置处理器将需要的Bean注入进来。

首先，我们定义个关注`BeanFactory`的接口：

```
public interface `BeanFactoryAware {
	void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
```

第二，定义一个`BeanFactory`的后置处理器，实现一旦`BeanFactoryAware`创建成功，将`BeanFactory`注入进去

```
@Component
public class BeanFactoryAwareProcessor implements BeanPostProcessor {
	private final BeanFactory bf;

	@Autowired
	public BeanFactoryAwareProcessor(ConfigurableApplicationContext appCtx) {
		this.bf = appCtx.getBeanFactory();
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof BeanFactoryAware) {
			BeanFactoryAware bfa = (BeanFactoryAware) bean;
			// 注入
			bfa.setBeanFactory(this.bf);
		}
		return bean;
	}
}
```

测试代码:

```
@Component
public class Bar implements BeanFactoryAware {
	private BeanFactory bf;

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.bf = beanFactory;
	}

	public BeanFactory getBf() {
		return bf;
	}
}

@ContextConfiguration
@ComponentScan
public class PostProcessorConfig {
}

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PostProcessorConfig.class)
public class PostProcessorConfigTest {
	@Autowired
	private Bar bar;

	@Test
	public void beanPostProcessor_BeanFacotryAwareCalled() {
		assertNotNull(bar);
		assertNotNull(bar.getBf());
		assertSame(bar, bar.getBf().getBean("bar"));
	}
}
```

## 总结

- 后置处理器统一对Bean的创建前和创建后进行处理
- XXAware接口，实现统一注入某些Bean，如ApplicationContextAware