package xunshan.spring.bean_instantiation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MyBeanFactory extends AbstractBeanFactory implements BeanDefinitionRegistry {
	private Map<String, Object> instanceMap = new ConcurrentHashMap<>(16);
	private Map<String, Class<?>> classMap = new ConcurrentHashMap<>(16);
	private Map<String, BeanDefinition> bdfMap = new ConcurrentHashMap<>(16);
	private Map<Class<?>, Object> classObjectMap = new ConcurrentHashMap<>(16);

	public String[] getBeanDefinitionNames() {
		return (String[]) bdfMap.keySet().toArray();
	}

	public int getBeanDefinitionCount() {
		return bdfMap.size();
	}

	public void registerBeanDefinition(
			String beanName,
			BeanDefinition beanDefinition)
			throws BeanDefinitionStoreException {
		Objects.requireNonNull(beanDefinition);

		if (!bdfMap.containsKey(beanName)) {
			bdfMap.put(beanName, beanDefinition);
			classMap.put(beanName, getClassByName(beanDefinition.getBeanClassName()));
		}
	}

	private Class<?> getClassByName(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
		bdfMap.remove(beanName);
	}

	public boolean containsBeanDefinition(String beanName) {
		return instanceMap.keySet().contains(beanName);
	}


	public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
		return this.bdfMap.get(beanName);
	}

	protected Object createBean(
			String beanName,
			RootBeanDefinition mbd,
			Object[] args) throws BeanCreationException {
		return null;
	}

	public Object getBean(String beanName) {
		/*Object instance = instanceMap.get(beanName);
		if (instance == null) {
			BeanDefinition bdf = bdfMap.get(beanName);
			instance = createBean(beanName, bdf, (Object[]) null);
		}*/
		return null;
	}

	public <T> T getBean(Class<T> requiredType) throws BeansException {
		return getBean(requiredType, null);
	}

	public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
		return null;
	}
}
