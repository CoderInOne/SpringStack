package xunshan.spring.post_processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

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
