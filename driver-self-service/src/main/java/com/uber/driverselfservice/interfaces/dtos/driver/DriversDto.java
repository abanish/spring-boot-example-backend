package com.uber.driverselfservice.interfaces.dtos.driver;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

/**
 * The DriversDto class is a data transfer object (DTO) that contains a list of drivers.
 */
public class DriversDto extends RepresentationModel {
	private List<DriverDto> drivers;

	public DriversDto() {}

	public DriversDto(List<DriverDto> drivers) {
		this.drivers = drivers;
	}

	public List<DriverDto> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<DriverDto> drivers) {
		this.drivers = drivers;
	}
}