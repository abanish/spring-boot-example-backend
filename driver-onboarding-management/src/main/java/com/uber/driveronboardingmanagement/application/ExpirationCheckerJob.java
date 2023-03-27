package com.uber.driveronboardingmanagement.application;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.uber.driveronboardingmanagement.domain.driveronboarding.DriverOnboardingExpiredEvent;
import com.uber.driveronboardingmanagement.domain.driveronboarding.DriverOnboardingRequestAggregateRoot;
import com.uber.driveronboardingmanagement.infrastructure.DriverSelfServiceMessageProducer;
import com.uber.driveronboardingmanagement.infrastructure.DriverOnboardingRequestRepository;

/**
 * ExpirationCheckerJob is a Quartz job that periodically checks for expired driver onboardings. For each
 * expired driver onboarding, it also sends an DriverOnboardingExpiredEvent to the Driver Self-Service backend.
 * */
public class ExpirationCheckerJob implements Job {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DriverSelfServiceMessageProducer driverSelfServiceMessageProducer;

	@Autowired
	private DriverOnboardingRequestRepository driverOnboardingRequestRepository;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		/*logger.debug("Checking for expired driver onboardings...");

		final Date date = new Date();
		List<DriverOnboardingRequestAggregateRoot> onboardingRequests = driverOnboardingRequestRepository.findAll();
		List<DriverOnboardingRequestAggregateRoot> expiredQuoteRequests = onboardingRequests.stream()
				.filter(onboardingRequest -> onboardingRequest.checkOnboardingExpirationDate(date))
				.collect(Collectors.toList());
		driverOnboardingRequestRepository.saveAll(expiredQuoteRequests);
		expiredQuoteRequests.forEach(expiredQuoteRequest -> {
			DriverOnboardingExpiredEvent event = new DriverOnboardingExpiredEvent(date, expiredQuoteRequest.getId());
			driverSelfServiceMessageProducer.sendDriverOnboardingExpiredEvent(event);
		});

		if(expiredQuoteRequests.size() > 0) {
			logger.info("Found {} expired driver onboardings", expiredQuoteRequests.size());
		} else {
			logger.debug("Found no expired driver onboardings");
		}*/
	}
}
