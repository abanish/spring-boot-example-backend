package com.uber.driveronboardingmanagement.interfaces.dtos.driver;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

/**
 * The DriversDto class is a data transfer object (DTO) that contains a list of drivers.
 * It inherits from the ResourceSupport class which allows us to create a REST representation (e.g., JSON, XML)
 * that follows the HATEOAS principle. For example, links can be added to the representation (e.g., self, next, prev)
 * which means that future actions the client may take can be discovered from the resource representation.
 *
 * @see <a href="https://docs.spring.io/spring-hateoas/docs/current/reference/html/">Spring HATEOAS - Reference Documentation</a>
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