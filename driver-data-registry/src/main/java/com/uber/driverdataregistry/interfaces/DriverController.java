package com.uber.driverdataregistry.interfaces;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.google.common.primitives.Bytes;
import com.uber.driverdataregistry.domain.driver.DriverAggregateRoot;
import com.uber.driverdataregistry.domain.driver.DriverId;
import com.uber.driverdataregistry.interfaces.dtos.driver.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import com.uber.driverdataregistry.application.DriverService;
import com.uber.driverdataregistry.application.Page;
import com.uber.driverdataregistry.domain.driver.Vehicle;
import com.uber.driverdataregistry.domain.driver.DriverProfileEntity;
import com.uber.driverdataregistry.interfaces.dtos.driver.DriversResponseDto;

@RestController
@RequestMapping("/drivers")
public class DriverController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DriverService driverService;
	
	private Set<String> getIncludedFields(String fields) {
		if (fields.trim().isEmpty()) {
			return Collections.emptySet();
		} else {
			return new HashSet<>(Arrays.asList(fields.split(",")));
		}
	}

	private DriverResponseDto createDriverResponseDto(DriverAggregateRoot driver, String fields) {
		final Set<String> includedFields = getIncludedFields(fields);
		DriverResponseDto driverResponseDto = new DriverResponseDto(includedFields, driver);
		Link selfLink = linkTo(
				methodOn(DriverController.class).getDriver(driver.getId().toString(), fields))
				.withSelfRel();

		Link updateVehicleLink = linkTo(methodOn(DriverController.class).changeVehicle(driver.getId(), null))
				.withRel("Vehicle.change");

		driverResponseDto.add(selfLink);
		driverResponseDto.add(updateVehicleLink);
		return driverResponseDto;
	}

	private PaginatedDriverResponseDto createPaginatedDriverResponseDto(String filter, Integer limit,
			Integer offset, int size, String fields, List<DriverResponseDto> driverDtos) {

		PaginatedDriverResponseDto paginatedDriverResponseDto = new PaginatedDriverResponseDto(filter, limit,
				offset, size, driverDtos);

		paginatedDriverResponseDto
		.add(linkTo(methodOn(DriverController.class).getDrivers(filter, limit, offset, fields))
				.withSelfRel());

		if (offset > 0) {
			paginatedDriverResponseDto.add(linkTo(methodOn(DriverController.class).getDrivers(filter,
					limit, Math.max(0, offset - limit), fields)).withRel("prev"));
		}

		if (offset < size - limit) {
			paginatedDriverResponseDto.add(linkTo(
					methodOn(DriverController.class).getDrivers(filter, limit, offset + limit, fields))
					.withRel("next"));
		}

		return paginatedDriverResponseDto;
	}

	@Operation(summary = "Get all drivers in pages of 10 entries per page.")
	@GetMapping
	public ResponseEntity<PaginatedDriverResponseDto> getDrivers(
			@Parameter(description = "search terms to filter the drivers by name", required = false) @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
			@Parameter(description = "the maximum number of drivers per page", required = false) @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@Parameter(description = "the offset of the page's first driver", required = false) @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
			@Parameter(description = "a comma-separated list of the fields that should be included in the response", required = false) @RequestParam(value = "fields", required = false, defaultValue = "") String fields) {

		final String decodedFilter = UriUtils.decode(filter, "UTF-8");
		final Page<DriverAggregateRoot> driverPage = driverService.getDrivers(decodedFilter, limit, offset);
		List<DriverResponseDto> driverDtos = driverPage.getElements().stream().map(c -> createDriverResponseDto(c, fields)).collect(Collectors.toList());

		PaginatedDriverResponseDto paginatedDriverResponseDto = createPaginatedDriverResponseDto(
				filter,
				driverPage.getLimit(),
				driverPage.getOffset(),
				driverPage.getSize(),
				fields,
				driverDtos);
		return ResponseEntity.ok(paginatedDriverResponseDto);
	}

	@Operation(summary = "Get a specific set of drivers.")
	@GetMapping(value = "/{ids}")
	public ResponseEntity<DriversResponseDto> getDriver(
			@Parameter(description = "a comma-separated list of driver ids", required = true) @PathVariable String ids,
			@Parameter(description = "a comma-separated list of the fields that should be included in the response", required = false) @RequestParam(value = "fields", required = false, defaultValue = "") String fields) {

		List<DriverAggregateRoot> drivers = driverService.getDrivers(ids);
		List<DriverResponseDto> driverResponseDtos = drivers.stream()
				.map(driver -> createDriverResponseDto(driver, fields)).collect(Collectors.toList());
		DriversResponseDto driversResponseDto = new DriversResponseDto(driverResponseDtos);
		Link selfLink = linkTo(methodOn(DriverController.class).getDriver(ids, fields)).withSelfRel();
		driversResponseDto.add(selfLink);
		return ResponseEntity.ok(driversResponseDto);
	}

	@Operation(summary = "Update the profile of the driver with the given driver id")
	@PutMapping(value = "/{driverId}")
	public ResponseEntity<DriverResponseDto> updateDriver(
			@Parameter(description = "the driver's unique id", required = true) @PathVariable DriverId driverId,
			@Parameter(description = "the driver's updated profile", required = true) @Valid @RequestBody DriverProfileUpdateRequestDto requestDto) {
		final DriverProfileEntity updatedDriverProfile = requestDto.toDomainObject();

		Optional<DriverAggregateRoot> optDriver = driverService.updateDriverProfile(driverId, updatedDriverProfile);
		if(!optDriver.isPresent()) {
			final String errorMessage = "Failed to find a driver with id '" + driverId.toString() + "'.";
			logger.info(errorMessage);
			throw new DriverNotFoundException(errorMessage);
		}

		DriverAggregateRoot driver = optDriver.get();
		DriverResponseDto response = new DriverResponseDto(Collections.emptySet(), driver);
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Change a driver's Vehicle.")
	@PutMapping(value = "/{driverId}/vehicle") // State Transition Operation (Partial Update)
	public ResponseEntity<VehicleDto> changeVehicle(
			@Parameter(description = "the driver's unique id", required = true) @PathVariable DriverId driverId,
			@Parameter(description = "the driver's new vehicle", required = true) @Valid @RequestBody VehicleDto requestDto) {

		Vehicle updatedVehicle = requestDto.toDomainObject();
		Optional<DriverAggregateRoot> optDriver = driverService.updateVehicle(driverId, updatedVehicle);
		if (!optDriver.isPresent()) {
			final String errorMessage = "Failed to find a driver with id '" + driverId.toString() + "'.";
			logger.info(errorMessage);
			throw new DriverNotFoundException(errorMessage);
		}

		VehicleDto responseDto = VehicleDto.fromDomainObject(updatedVehicle);
		return ResponseEntity.ok(responseDto);
	}

	@Operation(summary = "Verify Driver")
	@PutMapping(value = "/{driverId}/verify") // State Transition Operation (Partial Update)
	public void verifyDriver(
			@Parameter(description = "the driver's unique id", required = true) @PathVariable DriverId driverId) {

		Optional<DriverAggregateRoot> optDriver = driverService.verifyDriver(driverId);
		if (!optDriver.isPresent()) {
			final String errorMessage = "Failed to find a driver with id '" + driverId.toString() + "'.";
			logger.info(errorMessage);
			throw new DriverNotFoundException(errorMessage);
		}
	}

	@Operation(summary = "Upload driver's Document.")
	@PostMapping(value = "/{driverId}/document") // State Transition Operation (Partial Update)
	public ResponseEntity<String> uploadDocument(
			@Parameter(description = "the driver's unique id", required = true) @PathVariable DriverId driverId,
			@Parameter(description = "the driver's document", required = true) @Valid @RequestParam("file") MultipartFile file) {
		try {
			Optional<DriverAggregateRoot> optDriver = driverService.addDocument(driverId, file);
			if (!optDriver.isPresent()) {
				final String errorMessage = "Failed to find a driver with id '" + driverId.toString() + "'.";
				logger.info(errorMessage);
				throw new DriverNotFoundException(errorMessage);
			}
			return ResponseEntity.ok("Upload Success");
		}
		catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Upload Failed");
		}

	}

	@Operation(summary = "Upload driver's Document.")
	@PostMapping(value = "/{driverId}/file") // State Transition Operation (Partial Update)
	public ResponseEntity<String> uploadDocumentFromSelfService(
			@Parameter(description = "the driver's unique id", required = true) @PathVariable DriverId driverId,
			@Parameter(description = "the driver's document", required = true) @Valid @RequestBody DocumentDto requestDto) {
		try {
			logger.info(requestDto.getDocumentName());
			logger.info(requestDto.getDocumentType());
			Optional<DriverAggregateRoot> optDriver = driverService.addDocumentSelfService(driverId, requestDto);
			if (!optDriver.isPresent()) {
				final String errorMessage = "Failed to find a driver with id '" + driverId.toString() + "'.";
				logger.info(errorMessage);
				throw new DriverNotFoundException(errorMessage);
			}
			return ResponseEntity.ok("Upload Success");
		}
		catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Upload Failed");
		}

	}

	//TODO: Download document feature

	@Operation(summary = "Create a new driver.")
	@PostMapping
	public ResponseEntity<DriverResponseDto> createDriver(
			@Parameter(description = "the driver's profile information", required = true) @Valid @RequestBody DriverProfileUpdateRequestDto requestDto) {
		logger.info(requestDto.getLocality());
		DriverProfileEntity driverProfile = requestDto.toDomainObject();
		DriverAggregateRoot driver = driverService.createDriver(driverProfile);
		return ResponseEntity.ok(createDriverResponseDto(driver, ""));
	}
}
