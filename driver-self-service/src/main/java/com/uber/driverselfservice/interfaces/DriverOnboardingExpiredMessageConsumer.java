package com.uber.driverselfservice.interfaces;

import java.util.Optional;

import com.uber.driverselfservice.domain.driveronboarding.DriverOnboardingRequestAggregateRoot;
import com.uber.driverselfservice.infrastructure.DriverOnboardingRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.uber.driverselfservice.domain.driveronboarding.DriverOnboardingExpiredEvent;

/**
 * DriverOnboardingExpiredMessageConsumer is a Spring component that consumes DriverOnboardingExpiredEvents
 * as they arrive through the ActiveMQ message queue. It processes these events by marking the corresponding
 * driver onboarding requests as expired.
 * */
@Component
public class DriverOnboardingExpiredMessageConsumer {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DriverOnboardingRequestRepository driverOnboardingRequestRepository;

	@JmsListener(destination = "${driverOnboardingExpiredEvent.queueName}")
	public void receiveDriverOnboardingExpiredEvent(final Message<DriverOnboardingExpiredEvent> message) {
		logger.info("A new DriverOnboardingResponseEvent has been received.");
		final DriverOnboardingExpiredEvent driverOnboardingExpiredEvent = message.getPayload();
		final Long id = driverOnboardingExpiredEvent.getDriverOnboardingRequestId();
		final Optional<DriverOnboardingRequestAggregateRoot> driverOnboardingRequestOpt = driverOnboardingRequestRepository.findById(id);

		if(!driverOnboardingRequestOpt.isPresent()) {
			logger.error("Unable to process an driver onboarding expired event with an invalid driver onboarding request id.");
			return;
		}

		final DriverOnboardingRequestAggregateRoot driverOnboardingRequest = driverOnboardingRequestOpt.get();
		driverOnboardingRequest.markOnboardingAsExpired(driverOnboardingExpiredEvent.getDate());
		logger.info("The driver onboarding for driver onboarding request " + driverOnboardingRequest.getId() + " has expired.");
		driverOnboardingRequestRepository.save(driverOnboardingRequest);
	}
}