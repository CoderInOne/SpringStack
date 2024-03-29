package xunshan.spring.post_processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class LoggerBeanPostProcessor implements BeanPostProcessor {
	public LoggerBeanPostProcessor() {
		System.out.println("init logger processor");
	}

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
