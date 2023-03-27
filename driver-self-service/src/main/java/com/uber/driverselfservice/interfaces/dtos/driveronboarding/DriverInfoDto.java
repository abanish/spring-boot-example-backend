package com.uber.driverselfservice.interfaces.dtos.driveronboarding;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.uber.driverselfservice.domain.driver.DriverId;
import com.uber.driverselfservice.domain.driveronboarding.DriverInfoEntity;
import com.uber.driverselfservice.interfaces.dtos.driver.VehicleDto;

/**
 * DriverInfoDto is a data transfer object (DTO) that represents the
 * driver infos that are part of a Driver Onboarding Request.
 * */
public class DriverInfoDto {
	@NotEmpty
	private String driverId;

	@NotEmpty
	private String firstname;

	@NotEmpty
	private String lastname;

	@Valid
	@NotNull
	private VehicleDto currentVehicle;

	public DriverInfoDto() {
	}

	private DriverInfoDto(String driverId, String firstname, String lastname, VehicleDto currentVehicle) {
		this.driverId = driverId;
		this.firstname = firstname;
		this.lastname = lastname;
		this.currentVehicle = currentVehicle;
	}

	public static DriverInfoDto fromDomainObject(DriverInfoEntity driverInfo) {
		String driverId = driverInfo.getDriverId().getId();
		String firstname = driverInfo.getFirstname();
		String lastname = driverInfo.getLastname();
		VehicleDto contactVehicleDto = VehicleDto.fromDomainObject(driverInfo.getCurrentVehicle());
		return new DriverInfoDto(driverId, firstname, lastname, contactVehicleDto);
	}

	public DriverInfoEntity toDomainObject() {
		return new DriverInfoEntity(new DriverId(driverId), firstname, lastname, currentVehicle.toDomainObject());
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public VehicleDto getCurrentVehicle() {
		return currentVehicle;
	}

	public void setCurrentVehicle(VehicleDto currentVehicle) {
		this.currentVehicle = currentVehicle;
	}
}