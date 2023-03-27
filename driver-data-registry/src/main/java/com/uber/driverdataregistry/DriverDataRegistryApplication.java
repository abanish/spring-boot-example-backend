package com.uber.driverdataregistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
public class DriverDataRegistryApplication {
	private static Logger logger = LoggerFactory.getLogger(DriverDataRegistryApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DriverDataRegistryApplication.class, args);
		logger.info("--- Driver Data Registry started ---");
	}
}
