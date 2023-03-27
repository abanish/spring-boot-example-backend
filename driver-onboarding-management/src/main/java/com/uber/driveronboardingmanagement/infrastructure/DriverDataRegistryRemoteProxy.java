package com.uber.driveronboardingmanagement.infrastructure;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.uber.driveronboardingmanagement.interfaces.dtos.driver.DriverDto;
import com.uber.driveronboardingmanagement.interfaces.dtos.driver.DriversDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.uber.driveronboardingmanagement.domain.driver.DriverId;
import com.uber.driveronboardingmanagement.interfaces.dtos.DriverDataRegistryNotAvailableException;
import com.uber.driveronboardingmanagement.interfaces.dtos.driver.PaginatedDriverResponseDto;

/**
 * DriverDataRegistryRemoteProxy is a remote proxy that interacts with the Driver Core in order to give
 * the Onboarding Management Backend's own clients access to the shared driver data.
 * */
@Component
public class DriverDataRegistryRemoteProxy {
	@Value("${driverdataregistry.baseURL}")
	private String driverDataRegistryBaseURL;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RestTemplate restTemplate;


	public DriverDto getDriver(DriverId driverId) {
		List<DriverDto> drivers = getDriversById(driverId);
		return drivers.isEmpty() ? null : drivers.get(0);
	}

	public PaginatedDriverResponseDto getDrivers(String filter, int limit, int offset) {
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(driverDataRegistryBaseURL + "/drivers")
					.queryParam("filter", filter)
					.queryParam("limit", limit)
					.queryParam("offset", offset);
			return restTemplate.getForObject(builder.toUriString(), PaginatedDriverResponseDto.class);
		} catch(RestClientException e) {
			final String errorMessage = "Failed to connect to Driver Core.";
			logger.warn(errorMessage, e);
			throw new DriverDataRegistryNotAvailableException(errorMessage);
		}
	}

	public List<DriverDto> getDriversById(DriverId... driverIds) {
		try {
			List<String> driverIdStrings = Arrays.asList(driverIds).stream().map(id -> id.getId()).collect(Collectors.toList());
			String ids = String.join(",", driverIdStrings);
			return restTemplate.getForObject(driverDataRegistryBaseURL + "/drivers/" + ids, DriversDto.class).getDrivers();
		} catch(RestClientException e) {
			final String errorMessage = "Failed to connect to Driver Core.";
			logger.warn(errorMessage, e);
			throw new DriverDataRegistryNotAvailableException(errorMessage);
		}
	}
}
