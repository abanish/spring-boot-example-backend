package com.uber.driveronboardingmanagement.interfaces.dtos.driver;

import com.uber.driveronboardingmanagement.domain.driveronboarding.Vehicle;

/**
 * VehicleDto is a data transfer object (DTO) that represents the postal vehicle of a driver.
 * */
public class VehicleDto {
	private String vehicleName;
	private String licensePlateNumber;
	private String registeredCity;

	public VehicleDto() {
	}

	private VehicleDto(String vehicleName, String licensePlateNumber, String registeredCity) {
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
