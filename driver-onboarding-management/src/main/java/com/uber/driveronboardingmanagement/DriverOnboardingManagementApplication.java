package com.uber.driveronboardingmanagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

/**
 * OnboardingManagementApplication is the execution entry point of the Onboarding Management backend which
 * is one of the functional/system/application Bounded Contexts of Uber.
 */
@SpringBootApplication
@EnableJms
public class DriverOnboardingManagementApplication {
    private static Logger logger = LoggerFactory.getLogger(DriverOnboardingManagementApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DriverOnboardingManagementApplication.class, args);
        logger.info("--- Driver Onboarding Management started ---");
    }
}
