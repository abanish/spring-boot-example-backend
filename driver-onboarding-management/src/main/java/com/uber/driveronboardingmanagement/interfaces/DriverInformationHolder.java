package com.uber.driveronboardingmanagement.interfaces;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import com.uber.driveronboardingmanagement.domain.driver.DriverId;
import com.uber.driveronboardingmanagement.interfaces.dtos.driver.PaginatedDriverResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uber.driveronboardingmanagement.infrastructure.DriverDataRegistryRemoteProxy;
import com.uber.driveronboardingmanagement.interfaces.dtos.driver.DriverDto;
import com.uber.driveronboardingmanagement.interfaces.dtos.driver.DriverIdDto;
import com.uber.driveronboardingmanagement.interfaces.dtos.driver.DriverNotFoundException;

/**
 * This REST controller gives clients access to the driver data.
 */
@RestController
@RequestMapping("/drivers")
public class DriverInformationHolder {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DriverDataRegistryRemoteProxy driverDataRegistryRemoteProxy;

	@Operation(summary = "Get all drivers.")
	@GetMapping
	public ResponseEntity<PaginatedDriverResponseDto> getDrivers(
			@Parameter(description = "search terms to filter the drivers by name", required = false) @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
			@Parameter(description = "the maximum number of drivers per page", required = false) @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@Parameter(description = "the offset of the page's first driver", required = false) @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
		logger.debug("Fetching a page of drivers (offset={},limit={},filter='{}')", offset, limit, filter);
		PaginatedDriverResponseDto paginatedResponseIn = driverDataRegistryRemoteProxy.getDrivers(filter, limit, offset);
		PaginatedDriverResponseDto paginatedResponseOut = createPaginatedDriverResponseDto(
				paginatedResponseIn.getFilter(),
				paginatedResponseIn.getLimit(),
				paginatedResponseIn.getOffset(),
				paginatedResponseIn.getSize(),
				paginatedResponseIn.getDrivers());
		return ResponseEntity.ok(paginatedResponseOut);
	}

	private PaginatedDriverResponseDto createPaginatedDriverResponseDto(String filter, Integer limit, Integer offset, int size, List<DriverDto> driverDtos) {
		driverDtos.forEach(this::addDriverLinks);
		PaginatedDriverResponseDto paginatedDriverResponseDto = new PaginatedDriverResponseDto(filter, limit, offset, size, driverDtos);
		paginatedDriverResponseDto.add(linkTo(methodOn(DriverInformationHolder.class).getDrivers(filter, limit, offset)).withSelfRel());

		if (offset > 0) {
			paginatedDriverResponseDto.add(linkTo(
					methodOn(DriverInformationHolder.class).getDrivers(filter, limit, Math.max(0, offset - limit)))
					.withRel("prev"));
		}

		if (offset < size - limit) {
			paginatedDriverResponseDto.add(linkTo(methodOn(DriverInformationHolder.class).getDrivers(filter, limit, offset + limit))
					.withRel("next"));
		}

		return paginatedDriverResponseDto;
	}


	private void addDriverLinks(DriverDto driverDto) {
		DriverIdDto driverId = new DriverIdDto(driverDto.getDriverId());
		Link selfLink = linkTo(methodOn(DriverInformationHolder.class).getDriver(driverId)).withSelfRel();
		driverDto.add(selfLink);
	}

	@Operation(summary = "Get driver with a given driver id.")
	@GetMapping(value = "/{driverIdDto}")
	public ResponseEntity<DriverDto> getDriver(
			@Parameter(description = "the driver's unique id", required = true) @PathVariable DriverIdDto driverIdDto) {
		DriverId driverId = new DriverId(driverIdDto.getId());
		logger.debug("Fetching a driver with id '{}'", driverId.getId());
		DriverDto driver = driverDataRegistryRemoteProxy.getDriver(driverId);
		if(driver == null) {
			final String errorMessage = "Failed to find a driver with id '{}'";
			logger.warn(errorMessage, driverId.getId());
			throw new DriverNotFoundException(errorMessage);
		}

		addDriverLinks(driver);
		return ResponseEntity.ok(driver);
	}
}
