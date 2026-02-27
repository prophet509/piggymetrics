package com.piggymetrics.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;

@SpringBootTest(properties = {
    "spring.config.import=optional:configserver:",
    "eureka.client.enabled=false"
})
public class GatewayApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void fire() {

	}

}
