package xunshan.spring.bean_definition;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import xunshan.spring.bean_instantiation.Foo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
