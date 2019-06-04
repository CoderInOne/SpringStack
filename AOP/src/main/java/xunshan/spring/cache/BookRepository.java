package xunshan.spring.cache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class BookRepository {
	@Cacheable(value = "books", cacheManager = "books")
	Book findByTitle(String title) {
		System.out.println("calling findByTitle");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return new Book(title, "");
	}
}
