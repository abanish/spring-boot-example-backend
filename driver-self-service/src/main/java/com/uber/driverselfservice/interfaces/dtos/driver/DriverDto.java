package com.uber.driverselfservice.interfaces.dtos.driver;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * The DriverDto class is a data transfer object (DTO) that represents a single driver.
 */
public class DriverDto extends RepresentationModel {
	private String driverId;
	@JsonUnwrapped
	private DriverProfileDto driverProfile;

	public DriverDto() {
	}

	public String getDriverId() {
		return driverId;
	}

	public DriverProfileDto getDriverProfile() {
		return this.driverProfile;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public void setDriverProfile(DriverProfileDto driverProfile) {
		this.driverProfile = driverProfile;
	}
}
