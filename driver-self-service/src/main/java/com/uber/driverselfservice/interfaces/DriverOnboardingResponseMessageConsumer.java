package com.uber.driverselfservice.interfaces;

import java.util.Date;
import java.util.Optional;

import com.uber.driverselfservice.domain.driveronboarding.DriverOnboardingRequestAggregateRoot;
import com.uber.driverselfservice.domain.driveronboarding.DriverOnboardingResponseEvent;
import com.uber.driverselfservice.infrastructure.DriverDataRegistryRemoteProxy;
import com.uber.driverselfservice.infrastructure.DriverOnboardingRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * DriverOnboardingResponseMessageConsumer is a Spring component that consumes DriverOnboardingResponseEvents
 * as they arrive through the ActiveMQ message queue. It processes these events by updating the status
 * of the corresponding driver onboarding requests.
 * */
@Component
public class DriverOnboardingResponseMessageConsumer {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DriverOnboardingRequestRepository driverOnboardingRequestRepository;

	@Autowired
	private DriverDataRegistryRemoteProxy driverDataRegistryRemoteProxy;

	@JmsListener(destination = "${driverOnboardingResponseEvent.queueName}")
	public void receiveDriverOnboardingResponse(final Message<DriverOnboardingResponseEvent> message) {
		logger.info("A new DriverOnboardingResponseEvent has been received.");
		final DriverOnboardingResponseEvent driverOnboardingResponseEvent = message.getPayload();
		final Long id = driverOnboardingResponseEvent.getDriverOnboardingRequestId();
		final Optional<DriverOnboardingRequestAggregateRoot> driverOnboardingRequestOpt = driverOnboardingRequestRepository.findById(id);

		if(!driverOnboardingRequestOpt.isPresent()) {
			logger.error("Unable to process an driver onboarding response event with an invalid driver onboarding request id.");
			return;
		}

		final DriverOnboardingRequestAggregateRoot driverOnboardingRequest = driverOnboardingRequestOpt.get();
		if(driverOnboardingResponseEvent.isRequestAccepted()) {
			logger.info("The driver onboarding request " + driverOnboardingRequest.getId() + " has been accepted.");
			driverDataRegistryRemoteProxy.verifyDriver(driverOnboardingRequest.getDriverInfo().getDriverId());
		} else {
			logger.info("The driver onboarding request " + driverOnboardingRequest.getId() + " has been rejected.");
		}
		driverOnboardingRequestRepository.save(driverOnboardingRequest);
	}
}
