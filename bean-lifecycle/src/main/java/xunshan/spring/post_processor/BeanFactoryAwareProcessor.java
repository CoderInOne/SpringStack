package xunshan.spring.post_processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

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
			bfa.setBeanFactory(this.bf);
		}
		return bean;
	}
}
