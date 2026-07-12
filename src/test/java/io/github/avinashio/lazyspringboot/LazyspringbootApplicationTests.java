package io.github.avinashio.lazyspringboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "lazyspringboot.tui.enabled=false")
class LazyspringbootApplicationTests {

	@Test
	void contextLoads() {}
}
