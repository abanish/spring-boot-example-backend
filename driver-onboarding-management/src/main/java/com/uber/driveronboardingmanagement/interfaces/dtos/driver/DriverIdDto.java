package com.uber.driveronboardingmanagement.interfaces.dtos.driver;

/**
 * DriverIdDto is a data transfer object (DTO) that represents the unique ID of a driver.
 * */
public class DriverIdDto {
	private String id;

	public DriverIdDto() {
	}

	/**
	 * This constructor is needed by ControllerLinkBuilder, see the following
	 * spring-hateoas issue for details:
	 * https://github.com/spring-projects/spring-hateoas/issues/352
	 */
	public DriverIdDto(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return id;
	}
}