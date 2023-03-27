package com.uber.driveronboardingmanagement.interfaces;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.uber.driveronboardingmanagement.domain.driver.DriverId;
import com.uber.driveronboardingmanagement.domain.driveronboarding.*;
import com.uber.driveronboardingmanagement.infrastructure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.uber.driveronboardingmanagement.domain.driveronboarding.DriverInfoEntity;
import com.uber.driveronboardingmanagement.infrastructure.DriverSelfServiceMessageProducer;
import com.uber.driveronboardingmanagement.interfaces.dtos.driver.DriverDto;

/**
 * DriverDecisionMessageConsumer is a Spring component that consumes DriverDecisionEvents
 * as they arrive through the ActiveMQ message queue. It processes these events by updating the
 * corresponding driver onboarding requests.
 * */
@Component
public class DriverDecisionMessageConsumer {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DriverOnboardingRequestRepository driverOnboardingRequestRepository;

	@Autowired
	private DriverSelfServiceMessageProducer driverSelfServiceMessageProducer;

	@Autowired
	private DriverDataRegistryRemoteProxy driverDataRegistryRemoteProxy;

}
