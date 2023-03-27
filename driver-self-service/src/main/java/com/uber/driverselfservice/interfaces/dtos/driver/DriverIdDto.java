package com.uber.driverselfservice.interfaces.dtos.driver;

/**
 * DriverIdDto is a data transfer object (DTO) that represents the unique ID of a driver.
 * */
public class DriverIdDto {
	private String id;

	public DriverIdDto() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
