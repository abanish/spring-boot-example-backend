package com.uber.driverselfservice.interfaces;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.uber.driverselfservice.domain.driver.DriverId;
import com.uber.driverselfservice.infrastructure.DriverDataRegistryRemoteProxy;
import com.uber.driverselfservice.infrastructure.DriverOnboardingRequestRepository;
import com.uber.driverselfservice.interfaces.dtos.driver.*;
import com.uber.driverselfservice.interfaces.dtos.driveronboarding.DriverOnboardingRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.uber.driverselfservice.domain.identityaccess.UserLoginEntity;
import com.uber.driverselfservice.domain.driveronboarding.DriverOnboardingRequestAggregateRoot;
import com.uber.driverselfservice.infrastructure.UserLoginRepository;
import com.uber.driverselfservice.interfaces.dtos.driver.DriverDto;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/drivers")
public class DriverController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserLoginRepository userLoginRepository;

	@Autowired
	private DriverOnboardingRequestRepository driverOnboardingRequestRepository;

	@Autowired
	private DriverDataRegistryRemoteProxy driverDataRegistryRemoteProxy;

	@Operation(summary = "Change a driver's vehicle.")
	@PreAuthorize("isAuthenticated()")
	@PutMapping(value = "/{driverId}/vehicle")
	public ResponseEntity<VehicleDto> changeVehicle(
			@Parameter(description = "the driver's unique id", required = true) @PathVariable DriverId driverId,
			@Parameter(description = "the driver's new vehicle", required = true) @Valid @RequestBody VehicleDto requestDto) {
		return driverDataRegistryRemoteProxy.changeVehicle(driverId, requestDto);
	}

	@Operation(summary = "Upload a driver's document.")
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/{driverId}/document")
	public ResponseEntity<String> uploadDocument(
			@Parameter(description = "the driver's unique id", required = true) @PathVariable DriverId driverId,
			@Parameter(description = "the driver's document", required = true) @Valid @RequestParam("file") MultipartFile file) {
		logger.info("------Upload file--------");
		logger.info(file.getName());
		return driverDataRegistryRemoteProxy.uploadDocument(driverId, file);
	}

	@Operation(summary = "Get driver with a given driver id.")
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{driverId}")
	public ResponseEntity<DriverDto> getDriver(
			Authentication authentication,
			@Parameter(description = "the driver's unique id", required = true) @PathVariable DriverId driverId) {

		DriverDto driver = driverDataRegistryRemoteProxy.getDriver(driverId);
		if(driver == null) {
			final String errorMessage = "Failed to find a driver with id '" + driverId.getId() + "'.";
			logger.info(errorMessage);
			throw new DriverNotFoundException(errorMessage);
		}

		addHATEOASLinks(driver);
		return ResponseEntity.ok(driver);
	}

	@Operation(summary = "Complete the registration of a new driver.")
	@PreAuthorize("isAuthenticated()")
	@PostMapping
	public ResponseEntity<DriverDto> registerDriver(
			Authentication authentication,
			@Parameter(description = "the driver's profile information", required = true) @Valid @RequestBody DriverRegistrationRequestDto requestDto) {
		String loggedInUserEmail = authentication.getName();
		logger.info(requestDto.toString());
		DriverProfileUpdateRequestDto dto = new DriverProfileUpdateRequestDto(
				requestDto.getFirstname(), requestDto.getLastname(), requestDto.getBirthday(), requestDto.getVehicleName(),
				requestDto.getLicensePlateNumber(), requestDto.getRegisteredCity(), loggedInUserEmail, requestDto.getPhoneNumber(), requestDto.getAddress().getLocality(), requestDto.getAddress().getPinCode(), requestDto.getAddress().getCity());
		logger.info(dto.toString());

		DriverDto driver = driverDataRegistryRemoteProxy.createDriver(dto);
		UserLoginEntity loggedInUser = userLoginRepository.findByEmail(loggedInUserEmail);
		loggedInUser.setDriverId(new DriverId(driver.getDriverId()));
		userLoginRepository.save(loggedInUser);

		addHATEOASLinks(driver);
		return ResponseEntity.ok(driver);
	}

	@Operation(summary = "Get a driver's driver onboarding requests.")
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{driverId}/driver-onboarding-requests")
	public ResponseEntity<List<DriverOnboardingRequestDto>> getDriverOnboardingRequests(
			@Parameter(description = "the driver's unique id", required = true) @PathVariable DriverId driverId) {
		List<DriverOnboardingRequestAggregateRoot> driverOnboardingRequests = driverOnboardingRequestRepository.findByDriverInfo_DriverIdOrderByDateDesc(driverId);
		List<DriverOnboardingRequestDto> driverOnboardingRequestDtos = driverOnboardingRequests.stream().map(DriverOnboardingRequestDto::fromDomainObject).collect(Collectors.toList());
		return ResponseEntity.ok(driverOnboardingRequestDtos);
	}

	private void addHATEOASLinks(DriverDto driverDto) {
		DriverId driverId = new DriverId(driverDto.getDriverId());
		Link selfLink = linkTo(methodOn(DriverController.class).getDriver(null, driverId))
				.withSelfRel();
		Link updateVehicleLink = linkTo(methodOn(DriverController.class).changeVehicle(driverId, null))
				.withRel("vehicle.change");
		// When getting the DTO from the proxy, it already contains links
		// pointing to the driver-core, so we first remove them ...
		driverDto.removeLinks();
		// and add our own:
		driverDto.add(selfLink);
		driverDto.add(updateVehicleLink);
	}
}
