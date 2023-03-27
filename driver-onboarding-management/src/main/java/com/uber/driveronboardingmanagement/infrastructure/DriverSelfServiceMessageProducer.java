package com.uber.driveronboardingmanagement.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.uber.driveronboardingmanagement.domain.driveronboarding.DriverOnboardingExpiredEvent;
import com.uber.driveronboardingmanagement.domain.driveronboarding.DriverOnboardingResponseEvent;

/**
 * DriverSelfServiceMessageProducer is an infrastructure service class that is used to notify the Driver Self-Service Backend
 * when Uber has responded to a driver's driver onboarding request (DriverOnboardingResponseEvent) or when a driver onboarding
 * has expired (DriverOnboardingExpiredEvent). These events are transmitted via an ActiveMQ message queue.
 * */
@Component
public class DriverSelfServiceMessageProducer {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${driverOnboardingResponseEvent.queueName}")
	private String onboardingResponseQueue;

	@Value("${driverOnboardingExpiredEvent.queueName}")
	private String onboardingExpiredQueue;

	@Autowired
	private JmsTemplate jmsTemplate;

	public void sendDriverOnboardingResponseEvent(DriverOnboardingResponseEvent event) {
		try {
			jmsTemplate.convertAndSend(onboardingResponseQueue, event);
			logger.info("Successfully sent an driver onboarding response to the Driver Self-Service backend.");
		} catch(JmsException exception) {
			logger.error("Failed to send an driver onboarding response to the Driver Self-Service backend.", exception);
		}
	}

	public void sendDriverOnboardingExpiredEvent(DriverOnboardingExpiredEvent event) {
		try {
			jmsTemplate.convertAndSend(onboardingExpiredQueue, event);
			logger.info("Successfully sent an driver onboarding expired event to the Driver Self-Service backend.");
		} catch(JmsException exception) {
			logger.error("Failed to send an driver onboarding expired event to the Driver Self-Service backend.", exception);
		}
	}
}