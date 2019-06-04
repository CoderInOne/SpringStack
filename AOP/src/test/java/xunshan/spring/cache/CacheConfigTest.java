package xunshan.spring.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CacheConfig.class)
public class CacheConfigTest {
	@Autowired
	private BookRepository bookRepository;

	@Test
	public void cache() {
		Book b = bookRepository.findByTitle("A");
		Book b1 = bookRepository.findByTitle("A");
		assertSame(b, b1);
	}
}