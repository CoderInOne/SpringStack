package xunshan.spring.multiple_constructor_bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * two autowired constructor will fail
 */
@Component
public class BeanMulpitpleCtr {
	private Bar bar;
	private Bar bar1;

	@Autowired
	public BeanMulpitpleCtr(@Qualifier("bar") Bar bar) {
		this.bar = bar;
	}

	@Autowired
	public BeanMulpitpleCtr(@Qualifier("bar") Bar bar, @Qualifier("bar1") Bar bar1) {
		this.bar = bar;
		this.bar1 = bar1;
	}
}
