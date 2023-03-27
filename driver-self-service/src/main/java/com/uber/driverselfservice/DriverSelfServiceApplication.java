package com.uber.driverselfservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

/**
 * DriverSelfServiceApplication is the execution entry point of the Driver Self-Service backend which
 * is one of the functional/system/application Bounded Contexts of Uber Driver.
 */
@SpringBootApplication
@EnableCircuitBreaker
public class DriverSelfServiceApplication {
	private static Logger logger = LoggerFactory.getLogger(DriverSelfServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DriverSelfServiceApplication.class, args);
		logger.info("--- Driver Self-Service backend started ---");
	}
}

