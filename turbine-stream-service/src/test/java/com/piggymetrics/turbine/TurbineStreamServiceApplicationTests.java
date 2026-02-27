package com.piggymetrics.turbine;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.config.import=optional:configserver:")
public class TurbineStreamServiceApplicationTests {

	@Test
	public void contextLoads() {
	}

}
