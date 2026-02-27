package com.piggymetrics.account;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(properties = "spring.config.import=optional:configserver:")
public class AccountServiceApplicationTests {

	@Container
	static MongoDBContainer mongo = new MongoDBContainer("mongo:7.0");

	@DynamicPropertySource
	static void mongoProps(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
	}

	@Test
	public void contextLoads() {

	}

}
