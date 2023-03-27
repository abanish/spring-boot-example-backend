package com.uber.driverselfservice.infrastructure;

import java.io.File;
import java.util.List;

import com.uber.driverselfservice.domain.driver.Document;
import com.uber.driverselfservice.domain.driver.DriverId;
import com.uber.driverselfservice.interfaces.dtos.driver.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.uber.driverselfservice.interfaces.dtos.driver.DriverDto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.multipart.MultipartFile;

/**
 * DriverDataRegistryRemoteProxy is a remote proxy that interacts with the Driver Data Registry in order to give
 * the Driver Self-Service Backend's own clients access to the shared driver data.
 * */
@Component
public class DriverDataRegistryRemoteProxy implements DriverDataRegistryServiceMBean {
	@Value("${driverdataregistry.baseURL}")
	private String driverDataRegistryBaseURL;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RestTemplate restTemplate;

	int successfulAttemptsCounter=0;
	int unsuccessfulAttemptsCounter=0;
	int fallBackMethodExecutionsCounter=0;

	// TODO test/demo that protected methods actually stops executing for defined time period,
	@HystrixCommand(
			fallbackMethod = "getDummyDriver" )
	public DriverDto getDriver(DriverId driverId) {
		try {
			final String url = driverDataRegistryBaseURL + "/drivers/" + driverId.getId();
			logger.info("About to GET driver with id " + driverId);
			List<DriverDto> drivers = restTemplate.getForObject(url, DriversDto.class).getDrivers();
			successfulAttemptsCounter++;
			return drivers.isEmpty() ? null : drivers.get(0);
		} catch(RestClientException e) {
			final String errorMessage = "Failed to connect to Driver Data Registry.";
			logger.info(errorMessage, e);
			unsuccessfulAttemptsCounter++;
			throw new DriverDataRegistryNotAvailableException(errorMessage);
		}
	}

	public DriverDto getDummyDriver(DriverId driverId) {
		logger.warn("Circuit Breaker active, failed to connect to Driver Data Registry!");

		// TODO return default driver form database
		fallBackMethodExecutionsCounter++;

		DriverDto driverCopy = new DriverDto();
		driverCopy.setDriverId(driverId.toString());
		DriverProfileDto emptyProfile = new DriverProfileDto();
		emptyProfile.setFirstname("WARNING: No first name info available");
		emptyProfile.setLastname("WARNING: No last name info available");
		emptyProfile.setEmail("it-support@lm.com");
		emptyProfile.setPhoneNumber("WARNING: No phone number info available");
		VehicleDto currentVehicle = new VehicleDto();
		currentVehicle.setVehicleName("n/a");
		currentVehicle.setLicensePlateNumber("n/a");
		currentVehicle.setRegisteredCity("n/a");
		emptyProfile.setCurrentVehicle(currentVehicle );
		driverCopy.setDriverProfile(emptyProfile);
		return driverCopy;
	}

	public ResponseEntity<VehicleDto> changeVehicle(DriverId driverId, VehicleDto requestDto) {
		try {
			final String url = driverDataRegistryBaseURL + "/drivers/" + driverId.getId() + "/document";
			final HttpEntity<VehicleDto> requestEntity = new HttpEntity<>(requestDto);
			return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, VehicleDto.class);
		} catch(RestClientException e) {
			final String errorMessage = "Failed to connect to Driver Data Registry.";
			logger.info(errorMessage, e);
			throw new DriverDataRegistryNotAvailableException(errorMessage);
		}
	}

	public void verifyDriver(DriverId driverId) {
		try {
			final String url = driverDataRegistryBaseURL + "/drivers/" + driverId.getId() + "/verify";
			restTemplate.put(url, null);
		} catch(RestClientException e) {
			final String errorMessage = "Failed to connect to Driver Data Registry.";
			logger.info(errorMessage, e);
			throw new DriverDataRegistryNotAvailableException(errorMessage);
		}
	}

	public ResponseEntity<String> uploadDocument(DriverId driverId, MultipartFile file) {
		try {
			final String url = driverDataRegistryBaseURL + "/drivers/" + driverId.getId() + "/file";
			DocumentDto requestDto = DocumentDto.fromDomainObject(new Document(file.getOriginalFilename(), file.getContentType(), file.getBytes()));
			logger.info("------Upload doc--------");
			final HttpEntity<DocumentDto> requestEntity = new HttpEntity<>(requestDto);
			return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		} catch(RestClientException e) {
			final String errorMessage = "Failed to connect to Driver Data Registry.";
			logger.info(errorMessage, e);
			throw new DriverDataRegistryNotAvailableException(errorMessage);
		} catch(Exception e) {
			final String errorMessage = "Upload Failed";
			logger.info(errorMessage , e);
			throw new RuntimeException(errorMessage);
		}
	}

	public DriverDto createDriver(DriverProfileUpdateRequestDto requestDto) {
		try {
			final String url = driverDataRegistryBaseURL + "/drivers";
			return restTemplate.postForObject(url, requestDto, DriverDto.class);
		} catch(RestClientException e) {
			final String errorMessage = "Failed to connect to Driver Data Registry.";
			logger.info(errorMessage, e);
			throw new DriverDataRegistryNotAvailableException(errorMessage);
		}
	}

	@Override
	public int getSuccessfullAttemptsCounter() {
		return successfulAttemptsCounter;
	}

	@Override
	public int getUnuccessfullAttemptsCounter() {
		return unsuccessfulAttemptsCounter;
	}

	@Override
	public int getFallbackMethodExecutionCounter() {
		return fallBackMethodExecutionsCounter;
	}


	@Override
	public void resetCounters() {
		successfulAttemptsCounter=0;
		unsuccessfulAttemptsCounter=0;
		fallBackMethodExecutionsCounter=0;
	}

}
