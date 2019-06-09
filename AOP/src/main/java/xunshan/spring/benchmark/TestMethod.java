package xunshan.spring.benchmark;

import org.springframework.stereotype.Component;

@Component
public class TestMethod {
	void log() {
		for (int i = 0; i < 100_000; i++) {}
	}

	void logNoAop() {
		for (int i = 0; i < 100_000; i++) {}
	}
}
