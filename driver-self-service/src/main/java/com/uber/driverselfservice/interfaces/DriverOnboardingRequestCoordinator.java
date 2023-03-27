package com.uber.driverselfservice.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.uber.driverselfservice.domain.driver.DriverId;
import com.uber.driverselfservice.domain.driveronboarding.DriverOnboardingRequestAggregateRoot;
import com.uber.driverselfservice.domain.driveronboarding.RequestStatus;
import com.uber.driverselfservice.infrastructure.DriverOnboardingManagementMessageProducer;
import com.uber.driverselfservice.interfaces.dtos.driveronboarding.DriverOnboardingRequestDto;
import com.uber.driverselfservice.interfaces.dtos.driveronboarding.DriverOnboardingResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uber.driverselfservice.domain.identityaccess.UserLoginEntity;
import com.uber.driverselfservice.domain.driveronboarding.DriverInfoEntity;
import com.uber.driverselfservice.infrastructure.DriverOnboardingRequestRepository;
import com.uber.driverselfservice.infrastructure.UserLoginRepository;
import com.uber.driverselfservice.interfaces.dtos.driveronboarding.DriverInfoDto;
import com.uber.driverselfservice.interfaces.dtos.driveronboarding.DriverOnboardingRequestNotFoundException;

@RestController
@RequestMapping("/driver-onboarding-requests")
public class DriverOnboardingRequestCoordinator {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DriverOnboardingRequestRepository driverOnboardingRequestRepository;

	@Autowired
	private UserLoginRepository userLoginRepository;

	@Autowired
	private DriverOnboardingManagementMessageProducer driverOnboardingManagementMessageProducer;

	/**
	 * This endpoint is only used for debugging purposes.
	 * */
	@Operation(summary = "Get all driver onboarding Requests.")
	@GetMapping /* MAP: Retrieval Operation */ 
	public ResponseEntity<List<DriverOnboardingRequestDto>> getDriverOnboardingRequests() {
		List<DriverOnboardingRequestAggregateRoot> onboardingRequests = driverOnboardingRequestRepository.findAllByOrderByDateDesc();
		List<DriverOnboardingRequestDto> onboardingRequestDtos = onboardingRequests.stream().map(DriverOnboardingRequestDto::fromDomainObject).collect(Collectors.toList());
		return ResponseEntity.ok(onboardingRequestDtos);
	}

	@Operation(summary = "Get a specific driver onboarding Request.")
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{driverOnboardingRequestId}") /* MAP: Retrieval Operation */
	public ResponseEntity<DriverOnboardingRequestDto> getDriverOnboardingRequest(
			Authentication authentication,
			@Parameter(description = "the driver onboarding request's unique id", required = true) @PathVariable Long driverOnboardingRequestId) {
		Optional<DriverOnboardingRequestAggregateRoot> optDriverOnboardingRequest = driverOnboardingRequestRepository.findById(driverOnboardingRequestId);
		if(!optDriverOnboardingRequest.isPresent()) {
			final String errorMessage = "Failed to find an driver onboarding request with id '" + driverOnboardingRequestId + "'.";
			logger.info(errorMessage);
			throw new DriverOnboardingRequestNotFoundException(errorMessage);
		}

		DriverOnboardingRequestAggregateRoot driverOnboardingRequest = optDriverOnboardingRequest.get();
		DriverId loggedInDriverId = userLoginRepository.findByEmail(authentication.getName()).getDriverId();
		if (!driverOnboardingRequest.getDriverInfo().getDriverId().equals(loggedInDriverId)) {
			logger.info("Can't access an driver onboarding Request of a different driver.");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		return ResponseEntity.ok(DriverOnboardingRequestDto.fromDomainObject(driverOnboardingRequest));
	}

	@Operation(summary = "Create a new driver onboarding Request.")
	@PreAuthorize("isAuthenticated()")
	@PostMapping
	public ResponseEntity<DriverOnboardingRequestDto> createDriverOnboardingRequest(
			Authentication authentication,
			@Parameter(description = "the driver onboarding request", required = true) @Valid @RequestBody DriverOnboardingRequestDto requestDto) {
		String loggedInUserEmail = authentication.getName();
		UserLoginEntity loggedInUser = userLoginRepository.findByEmail(loggedInUserEmail);
		DriverId loggedInDriverId = loggedInUser.getDriverId();
		if (loggedInDriverId == null) {
			logger.info("Driver needs to complete registration first.");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		DriverInfoDto driverInfoDto = requestDto.getDriverInfo();
		DriverId driverId = new DriverId(driverInfoDto.getDriverId());

		if (!driverId.equals(loggedInDriverId)) {
			logger.info("Can't create an driver onboarding Request for a different driver.");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		DriverInfoEntity driverInfoEntity = driverInfoDto.toDomainObject();

		final Date date = new Date();
		DriverOnboardingRequestAggregateRoot driverOnboardingRequest = new DriverOnboardingRequestAggregateRoot(date, RequestStatus.ONBOARDING_REQUEST_SUBMITTED, driverInfoEntity);
		driverOnboardingRequestRepository.save(driverOnboardingRequest);
		DriverOnboardingRequestDto responseDto = DriverOnboardingRequestDto.fromDomainObject(driverOnboardingRequest);

		driverOnboardingManagementMessageProducer.sendDriverOnboardingRequest(date, responseDto);

		return ResponseEntity.ok(responseDto);
	}

	@Operation(summary = "Updates the status of an existing driver onboarding Request")
	@PreAuthorize("isAuthenticated()")
	@PatchMapping(value = "/{id}") /* MAP: State Transition Operation */ 
	public ResponseEntity<DriverOnboardingRequestDto> respondToDriverOnboarding(
			Authentication authentication,
			@Parameter(description = "the driver onboarding request's unique id", required = true) @PathVariable Long id,
			@Parameter(description = "the response that contains the driver's decision whether to accept or reject an driver onboarding", required = true)
			@Valid @RequestBody DriverOnboardingResponseDto driverOnboardingResponseDto) {
		String loggedInUserEmail = authentication.getName();
		UserLoginEntity loggedInUser = userLoginRepository.findByEmail(loggedInUserEmail);
		DriverId loggedInDriverId = loggedInUser.getDriverId();
		if (loggedInDriverId == null) {
			logger.info("Driver needs to complete registration first.");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Optional<DriverOnboardingRequestAggregateRoot> optDriverOnboardingRequest = driverOnboardingRequestRepository.findById(id);
		if (!optDriverOnboardingRequest.isPresent()) {
			final String errorMessage = "Failed to respond to driver onboarding, because there is no driver onboarding request with id '" + id + "'.";
			logger.info(errorMessage);
			throw new DriverOnboardingRequestNotFoundException(errorMessage);
		}

		final DriverOnboardingRequestAggregateRoot driverOnboardingRequest = optDriverOnboardingRequest.get();
		DriverId driverId = driverOnboardingRequest.getDriverInfo().getDriverId();
		if (!driverId.equals(loggedInDriverId)) {
			logger.info("Can't update an driver onboarding Request of a different driver.");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		driverOnboardingRequestRepository.save(driverOnboardingRequest);

		DriverOnboardingRequestDto driverOnboardingRequestDto = DriverOnboardingRequestDto.fromDomainObject(driverOnboardingRequest);
		return ResponseEntity.ok(driverOnboardingRequestDto);
	}
}
