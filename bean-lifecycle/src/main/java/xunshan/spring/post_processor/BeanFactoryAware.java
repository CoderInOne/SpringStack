package xunshan.spring.post_processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

public interface BeanFactoryAware {
	void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
