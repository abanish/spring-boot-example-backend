package com.uber.driveronboardingmanagement.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.uber.driveronboardingmanagement.infrastructure.DriverSelfServiceMessageProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uber.driveronboardingmanagement.domain.driveronboarding.DriverOnboardingEntity;
import com.uber.driveronboardingmanagement.domain.driveronboarding.DriverOnboardingRequestAggregateRoot;
import com.uber.driveronboardingmanagement.domain.driveronboarding.DriverOnboardingResponseEvent;
import com.uber.driveronboardingmanagement.domain.driveronboarding.RequestStatus;
import com.uber.driveronboardingmanagement.infrastructure.DriverOnboardingRequestRepository;
import com.uber.driveronboardingmanagement.interfaces.dtos.driveronboarding.DriverOnboardingRequestDto;
import com.uber.driveronboardingmanagement.interfaces.dtos.driveronboarding.DriverOnboardingRequestNotFoundException;
import com.uber.driveronboardingmanagement.interfaces.dtos.driveronboarding.DriverOnboardingResponseDto;
/**
 * This REST controller gives clients access to the driver onboarding requests.
*/

@RestController
@RequestMapping("/driver-onboarding-requests")
public class DriverOnboardingRequestProcessingResource {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DriverOnboardingRequestRepository driverOnboardingRequestRepository;

	@Autowired
	private DriverSelfServiceMessageProducer driverSelfServiceMessageProducer;

	@Operation(summary = "Get all Driver Onboarding Requests.")
	@GetMapping
	public ResponseEntity<List<DriverOnboardingRequestDto>> getDriverOnboardingRequests() {
		logger.debug("Fetching all Driver Onboarding Requests");
		List<DriverOnboardingRequestAggregateRoot> onboardingRequests = driverOnboardingRequestRepository.findAllByOrderByDateDesc();
		List<DriverOnboardingRequestDto> onboardingRequestDtos = onboardingRequests.stream().map(DriverOnboardingRequestDto::fromDomainObject).collect(Collectors.toList());
		return ResponseEntity.ok(onboardingRequestDtos);
	}

	@Operation(summary = "Get a specific Driver Onboarding Request.")
	@GetMapping(value = "/{id}") /* MAP: Retrieval Operation */
	public ResponseEntity<DriverOnboardingRequestDto> getDriverOnboardingRequest(@Parameter(description = "the driver onboarding request's unique id", required = true) @PathVariable Long id) {
		logger.debug("Fetching Driver Onboarding Request with id '{}'", id);
		Optional<DriverOnboardingRequestAggregateRoot> optDriverOnboardingRequest = driverOnboardingRequestRepository.findById(id);
		if (!optDriverOnboardingRequest.isPresent()) {
			final String errorMessage = "Failed to find an Driver Onboarding Request with id '{}'";
			logger.warn(errorMessage, id);
			throw new DriverOnboardingRequestNotFoundException(errorMessage);
		}

		DriverOnboardingRequestAggregateRoot driverOnboardingRequest = optDriverOnboardingRequest.get();
		DriverOnboardingRequestDto driverOnboardingRequestDto = DriverOnboardingRequestDto.fromDomainObject(driverOnboardingRequest);
		return ResponseEntity.ok(driverOnboardingRequestDto);
	}

	@Operation(summary = "Updates the status of an existing Driver Onboarding Request")
	@PatchMapping(value = "/{id}") /* MAP: State Transition Operation */
	public ResponseEntity<DriverOnboardingRequestDto> respondToDriverOnboardingRequest(
			@Parameter(description = "the driver onboarding request's unique id", required = true) @PathVariable Long id,
			@Parameter(description = "the response that contains a new driver onboarding if the request has been accepted", required = true)
			@Valid @RequestBody DriverOnboardingResponseDto driverOnboardingResponseDto) {

		logger.debug("Responding to Driver Onboarding Request with id '{}'", id);

		Optional<DriverOnboardingRequestAggregateRoot> optDriverOnboardingRequest = driverOnboardingRequestRepository.findById(id);
		if (!optDriverOnboardingRequest.isPresent()) {
			final String errorMessage = "Failed to respond to Driver Onboarding Request, because there is no Driver Onboarding Request with id '{}'";
			logger.warn(errorMessage, id);
			throw new DriverOnboardingRequestNotFoundException(errorMessage);
		}

		final Date date = new Date();
		final DriverOnboardingRequestAggregateRoot driverOnboardingRequest = optDriverOnboardingRequest.get();
		if(driverOnboardingResponseDto.getStatus().equals(RequestStatus.ONBOARDING_REQUEST_SUCCESS.toString())) {
			logger.info("Driver Onboarding Request with id '{}' has been accepted", id);

			Date expirationDate = driverOnboardingResponseDto.getExpirationDate();
			driverOnboardingRequest.acceptRequest(date);
			final DriverOnboardingResponseEvent driverOnboardingResponseEvent = new DriverOnboardingResponseEvent(date, driverOnboardingRequest.getId(), true, expirationDate);
			driverSelfServiceMessageProducer.sendDriverOnboardingResponseEvent(driverOnboardingResponseEvent);
		} else if(driverOnboardingResponseDto.getStatus().equals(RequestStatus.ONBOARDING_REQUEST_REJECTED.toString())) {
			logger.info("Driver Onboarding Request with id '{}' has been rejected", id);

			driverOnboardingRequest.rejectRequest(date);
			final DriverOnboardingResponseEvent driverOnboardingResponseEvent = new DriverOnboardingResponseEvent(date, driverOnboardingRequest.getId(), false, null);
			driverSelfServiceMessageProducer.sendDriverOnboardingResponseEvent(driverOnboardingResponseEvent);
		}
		driverOnboardingRequestRepository.save(driverOnboardingRequest);

		DriverOnboardingRequestDto driverOnboardingRequestDto = DriverOnboardingRequestDto.fromDomainObject(driverOnboardingRequest);
		return ResponseEntity.ok(driverOnboardingRequestDto);
	}
}