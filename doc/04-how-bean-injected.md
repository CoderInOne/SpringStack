# 依赖解析与注入

依赖注入很重要的一个优点在于不同层次之间不需要关心对方是如何创建的，比如controller层，要使用
service层，service层依赖了数据库，远程调用等，通过组装，service注册到容器中，然后注入到controller层，
使得controller只需要关心怎么用，不需要关心怎么创建，代码从而变得清晰，明了，便于维护。依赖注入
可以有效地连接不同的layer，同时又让每个layer专注于真正的业务逻辑，剩下的如对象的创建，让每个
layer自己去关心。

Bean的注入，可以直观地理解为容器查找并调用setter方法或者通过反射的方式设置的。
本节主题如下：

- 依赖解析
- 依赖注入


Spring有两种注入方式：

- Constructor Injection
- Setter Injection

我们讲的注入是指把创建好的对象注入到指定的目标中，本节涉及的源代码：

```
// setter injection
@Component
public class SetterInjection {
	private Foo foo;

	@Autowired
	public void setFoo(Foo foo) {
		this.foo = foo;
	}

	Foo getFoo() {
		return foo;
	}
}

// constructor injection
@Component
public class ConstructorInjection {
	private Foo foo;

	@Autowired
	public ConstructorInjection(Foo foo) {
		this.foo = foo;
	}

	public Foo getFoo() {
		return foo;
	}
}

// object provider
@Configuration
@ComponentScan
public class InjectionConfig {
	@Bean
	public Foo foo() {
		return new Foo();
	}
}
```

## 构造器注入

```
// 组装构造器
public BeanWrapper autowireConstructor(
            final String beanName, 
            final RootBeanDefinition mbd,
			@Nullable Constructor<?>[] chosenCtors, 
			@Nullable final Object[] explicitArgs)
// 创建每个参数	
argsHolder = createArgumentArray(beanName, mbd, resolvedValues, bw, paramTypes, paramNames,
           getUserDeclaredConstructor(candidate), autowiring);
           
// 解析依赖，这是组装里面的一个核心概念，
this.beanFactory.resolveDependency(
           new DependencyDescriptor(param, true), beanName, autowiredBeanNames, typeConverter);
           
// 创建对象，完成注入，注意被注入的对象在argsToUse中
beanInstance = strategy.instantiate(mbd, beanName, this.beanFactory, constructorToUse, argsToUse);
```

## Setter方法注入

```
// 创建完对象之后，"填充"bean里面的所有字段
protected void AbstractAutowireCapableBeanFactory#populateBean(
        String beanName, RootBeanDefinition mbd, BeanWrapper bw)
  
// 获取要"填充"的字段，回调InstantiationAwareBeanPostProcessor#postProcessPropertyValues
PropertyDescriptor[] filteredPds = filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
if (hasInstAwareBpps) {
    for (BeanPostProcessor bp : getBeanPostProcessors()) {
        if (bp instanceof InstantiationAwareBeanPostProcessor) {
            InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
            pvs = ibp.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName);
            if (pvs == null) {
                return;
            }
        }
    }
}

// 构建InjectionMetadata，注入元数据
InjectionMetadata#inject
```