package xunshan.spring.cache;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableCaching
@ComponentScan
public class CacheConfig {
	@Bean
	public CacheManager cm() {
		return new ConcurrentMapCacheManager("books");
	}

	@Bean
	public CacheManagerCustomizer<ConcurrentMapCacheManager> cacheMgrCtz() {
		return new CacheManagerCustomizer<ConcurrentMapCacheManager>() {
			public void customize(ConcurrentMapCacheManager cacheManager) {
				cacheManager.setAllowNullValues(false);
			}
		};
	}
}
