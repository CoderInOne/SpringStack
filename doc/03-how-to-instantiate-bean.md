# Bean的实例化

简单地，我们从断点开始。

```
@Bean
FooObjectFactory fooObjectFactory() {
    return new FooObjectFactory();  // <- debug
}
```

这里省去context的加载和刷新，直接进入主题。我们把堆栈贴出来，有个大概的了解：

```
fooObjectFactory:37, TestConfig (xunshan.spring.bean_instantiation)
CGLIB$fooObjectFactory$2:-1, TestConfig$$EnhancerBySpringCGLIB$$c0cb063c (xunshan.spring.bean_instantiation)
invoke:-1, TestConfig$$EnhancerBySpringCGLIB$$c0cb063c$$FastClassBySpringCGLIB$$5c83a7bd (xunshan.spring.bean_instantiation)
invokeSuper:228, MethodProxy (org.springframework.cglib.proxy)
intercept:361, ConfigurationClassEnhancer$BeanMethodInterceptor (org.springframework.context.annotation)
fooObjectFactory:-1, TestConfig$$EnhancerBySpringCGLIB$$c0cb063c (xunshan.spring.bean_instantiation)
invoke0:-1, NativeMethodAccessorImpl (sun.reflect)
invoke:62, NativeMethodAccessorImpl (sun.reflect)
invoke:43, DelegatingMethodAccessorImpl (sun.reflect)
invoke:498, Method (java.lang.reflect)
instantiate:154, SimpleInstantiationStrategy (org.springframework.beans.factory.support)
instantiateUsingFactoryMethod:582, ConstructorResolver (org.springframework.beans.factory.support)
instantiateUsingFactoryMethod:1247, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
createBeanInstance:1096, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
doCreateBean:535, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
createBean:495, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
lambda$doGetBean$0:317, AbstractBeanFactory (org.springframework.beans.factory.support)
getObject:-1, 2110756088 (org.springframework.beans.factory.support.AbstractBeanFactory$$Lambda$14)
getSingleton:222, DefaultSingletonBeanRegistry (org.springframework.beans.factory.support)
doGetBean:315, AbstractBeanFactory (org.springframework.beans.factory.support)
getBean:199, AbstractBeanFactory (org.springframework.beans.factory.support)
...

1. 判断是否在缓存中，有缓存则直接取出Bean
2. 无缓存，检查Bean定义，当前BeanFactory没有，则由父BeanFactory代之
3. 根据Scope创建Bean，并保存到缓存中
```

其中，创建对象的方式有两种：

- FactoryMethod: 通常是Bean和ObjectFactory这种
- Constructor：由对象本身的构造器创建

两种方法分别调用了SimpleInstantiationStrategy的两个方法：

- Constructor: SimpleInstantiationStrategy.instantiate(...)
- FactoryMethod: SimpleInstantiationStrategy.instantiate(..., Method factoryMethod, java.lang.Object...)

两种方式最终都是通过反射的方式完成。

## 策略模式

分析过程中，我们看到SimpleInstantiationStrategy是个策略模式的实现，和经典方式不同在于，SimpleInstantiationStrategy
将两种方式封装在不同方法中，不同策略调用不同方法。简单实用，未尝不可。接下来我们简单地分析一下策略模式的实现。

策略模式动机：根据不同的Bean定义完成Bean的创建，其中创建方式有工厂方法和构造器两种。

策略模式定义：定义一系列算法，将每一个算法封装起来，并让它们可以相互替换。策略模式让算法独立于使用它的客户而变化，也称为政策模式(Policy)。

策略模式结构：

- Context: 环境类，这里则是BeanDefinition，如果有factoryMethodName则是工厂方法方式
- Strategy: 抽象策略类，即InstantiationStrategy接口
- ConcreteStrategy: 具体策略类，SimpleInstantiationStrategy两个方法

首先，我们先找到核心算法，通过对比两种方法的堆栈，发现AbstractAutowireCapableBeanFactory#doCreateBean是核心算法，并且在createBeanInstance
这个方法中进行了策略的选择，看下具体代码：

```
protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) {
    // Make sure bean class is actually resolved at this point.
    Class<?> beanClass = resolveBeanClass(mbd, beanName);

    // ...

    // 策略一：工厂方法
    if (mbd.getFactoryMethodName() != null)  {
        return instantiateUsingFactoryMethod(beanName, mbd, args);
    }

    // Shortcut when re-creating the same bean...
    // 重复创建，快速返回

    // 策略二：构造器
    // Need to determine the constructor...
    Constructor<?>[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
    if (ctors != null ||
            mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_CONSTRUCTOR ||
            mbd.hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args))  {
        return autowireConstructor(beanName, mbd, ctors, args);
    }

    // No special handling: simply use no-arg constructor.
    return instantiateBean(beanName, mbd);
}
```

有一句话，概括地非常好，策略模式的本质是`准备一组算法，并将每一个算法封装起来，使得它们可以互换`。

## 总结

Bean的创建过程，还是非常直观的，因为我们知道对象要么是new创建的(对应FactoryMethod)，要么是反射调用构造器创建的(对应@Bean)。

不同的创建方法，同样的创建流程，策略模式使得代码的可用性，灵活性得以提高。最后有两道习题以供大家深入地理解，本节的
相关代码在bean-lifecycle包路径xunshan.spring.bean_instantiation下面。

## 练习

1. 通过debug方式分析构造器方式创建对象
2. 独立实现策略模式