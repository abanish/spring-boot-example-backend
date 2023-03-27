package com.uber.driveronboardingmanagement.interfaces.dtos.driver;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * DriverProfileDto is a data transfer object (DTO) that represents the personal data (driver profile) of a driver.
 */
public class DriverProfileDto {
	private String firstname;
	private String lastname;
	private Date birthday;
	@JsonUnwrapped
	private VehicleDto currentVehicle;
	private String email;
	private String phoneNumber;
	private List<VehicleDto> vehicleList;

	public DriverProfileDto() {
	}

	public DriverProfileDto(String firstname, String lastname, Date birthday, VehicleDto currentVehicle, String email, String phoneNumber, List<VehicleDto> vehicleList) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthday = birthday;
		this.currentVehicle = currentVehicle;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.vehicleList = vehicleList;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public Date getBirthday() {
		return birthday;
	}

	public VehicleDto getCurrentVehicle() {
		return currentVehicle;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public List<VehicleDto> getVehicleList() {
		return vehicleList;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setCurrentVehicle(VehicleDto currentVehicle) {
		this.currentVehicle = currentVehicle;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setVehicleList(List<VehicleDto> vehicleList) {
		this.vehicleList = vehicleList;
	}
}
