package com.uber.driveronboardingmanagement.interfaces;


import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.uber.driveronboardingmanagement.domain.driveronboarding.DriverInfoEntity;
import com.uber.driveronboardingmanagement.domain.driveronboarding.DriverOnboardingRequestAggregateRoot;
import com.uber.driveronboardingmanagement.domain.driveronboarding.DriverOnboardingRequestEvent;
import com.uber.driveronboardingmanagement.domain.driveronboarding.RequestStatus;
import com.uber.driveronboardingmanagement.infrastructure.DriverOnboardingRequestRepository;
import com.uber.driveronboardingmanagement.interfaces.dtos.driveronboarding.DriverOnboardingRequestDto;
import com.uber.driveronboardingmanagement.interfaces.dtos.driveronboarding.RequestStatusChangeDto;

/**
 * DriverOnboardingRequestMessageConsumer is a Spring component that consumes DriverOnboardingRequestEvents
 * as they arrive through the ActiveMQ message queue. It processes these events by creating corresponding
 * DriverOnboardingRequestAggregateRoot instances.
 * */
@Component
public class DriverOnboardingRequestMessageConsumer {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DriverOnboardingRequestRepository driverOnboardingRequestRepository;

	@JmsListener(destination = "${driverOnboardingRequestEvent.queueName}")
	public void receiveDriverOnboardingRequest(final Message<DriverOnboardingRequestEvent> message) {
		logger.info("A new DriverOnboardingRequestEvent has been received.");

		DriverOnboardingRequestEvent driverOnboardingRequestEvent = message.getPayload();
		DriverOnboardingRequestDto driverOnboardingRequestDto = driverOnboardingRequestEvent.getDriverOnboardingRequestDto();
		Long id = driverOnboardingRequestDto.getId();
		Date date = driverOnboardingRequestDto.getDate();
		List<RequestStatusChangeDto> statusHistory = driverOnboardingRequestDto.getStatusHistory();
		RequestStatus status = RequestStatus.valueOf(statusHistory.get(statusHistory.size()-1).getStatus());

		DriverInfoEntity driverInfo = driverOnboardingRequestDto.getDriverInfo().toDomainObject();
		DriverOnboardingRequestAggregateRoot driverOnboardingAggregateRoot = new DriverOnboardingRequestAggregateRoot(id, date, status, driverInfo);
		driverOnboardingRequestRepository.save(driverOnboardingAggregateRoot);
	}
}
