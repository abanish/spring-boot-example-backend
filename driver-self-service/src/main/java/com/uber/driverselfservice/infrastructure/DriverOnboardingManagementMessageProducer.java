package com.uber.driverselfservice.infrastructure;

import java.util.Date;

import com.uber.driverselfservice.domain.driveronboarding.DriverOnboardingRequestEvent;
import com.uber.driverselfservice.interfaces.dtos.driveronboarding.DriverOnboardingRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * DriverOnboardingManagementMessageProducer is an infrastructure service class that is used to notify the Onboarding Management Backend
 * when a new driver onboarding request has been created (DriverOnboardingRequestEvent).
 * These events are transmitted via an ActiveMQ message queue.
 * */
@Component
public class DriverOnboardingManagementMessageProducer {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${driverOnboardingRequestEvent.queueName}")
	private String driverOnboardingRequestEventQueue;

	@Autowired
	private JmsTemplate jmsTemplate;

	public void sendDriverOnboardingRequest(Date date, DriverOnboardingRequestDto driverOnboardingRequestDto) {
		DriverOnboardingRequestEvent driverOnboardingRequestEvent = new DriverOnboardingRequestEvent(date, driverOnboardingRequestDto);
		emitDriverOnboardingRequestEvent(driverOnboardingRequestEvent);
	}

	private void emitDriverOnboardingRequestEvent(DriverOnboardingRequestEvent driverOnboardingRequestEvent) {
		try {
			jmsTemplate.convertAndSend(driverOnboardingRequestEventQueue, driverOnboardingRequestEvent);
			logger.info("Successfully sent a driver onboarding request to the Onboarding Management backend.");
		} catch(JmsException exception) {
			logger.error("Failed to send a driver onboarding request to the Onboarding Management backend.", exception);
		}
	}
}