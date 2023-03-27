package com.uber.driverselfservice.interfaces.dtos.driver;

import javax.validation.constraints.NotEmpty;

import com.uber.driverselfservice.domain.driver.Vehicle;

/**
 * VehicleDto is a data transfer object (DTO) that represents the vehicle of a driver.
 * */
public class VehicleDto {
	@NotEmpty
	private String vehicleName;

	@NotEmpty
	private String licensePlateNumber;

	@NotEmpty
	private String registeredCity;

	public VehicleDto() {
	}

	public VehicleDto(String vehicleName, String licensePlateNumber, String registeredCity) {
		this.vehicleName = vehicleName;
		this.licensePlateNumber = licensePlateNumber;
		this.registeredCity = registeredCity;
	}

	public static VehicleDto fromDomainObject(Vehicle vehicle) {
		return new VehicleDto(vehicle.getVehicleName(), vehicle.getLicensePlateNumber(), vehicle.getRegisteredCity());
	}

	public Vehicle toDomainObject() {
		return new Vehicle(vehicleName, licensePlateNumber, registeredCity);
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}

	public String getRegisteredCity() {
		return registeredCity;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public void setLicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}

	public void setRegisteredCity(String registeredCity) {
		this.registeredCity = registeredCity;
	}
}