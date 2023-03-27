package com.uber.driverdataregistry.interfaces.dtos.driver;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

/**
 * The DriversResponseDto holds a collection of @DriverResponseDto
 */
public class DriversResponseDto extends RepresentationModel {
	private final List<DriverResponseDto> drivers;

	public DriversResponseDto(List<DriverResponseDto> drivers) {
		this.drivers = drivers;
	}

	public List<DriverResponseDto> getDrivers() {
		return drivers;
	}
}
