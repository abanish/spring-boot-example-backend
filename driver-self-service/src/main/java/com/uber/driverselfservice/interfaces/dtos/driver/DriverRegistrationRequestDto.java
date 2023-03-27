package com.uber.driverselfservice.interfaces.dtos.driver;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Objects;
import com.uber.driverselfservice.domain.driver.Address;
import com.uber.driverselfservice.interfaces.validation.PhoneNumber;

/**
 * DriverRegistrationRequestDto is a data transfer object (DTO) that represents the personal data (driver profile) of a driver.
 * It is sent to the DriverController when a user completes the registration process in the Driver Self-Service frontend.
 */
public class DriverRegistrationRequestDto {

	@NotEmpty
	private String firstname;

	@NotEmpty
	private String lastname;

	@NotNull
	@Past
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date birthday;

	@NotEmpty
	private String registeredCity;

	@NotEmpty
	private String vehicleName;

	@NotEmpty
	private String licensePlateNumber;

	@PhoneNumber
	private String phoneNumber;

	@NotEmpty
	private Address address;

	public DriverRegistrationRequestDto() {
	}

	public Date getBirthday() {
		return birthday;
	}

	public String getRegisteredCity() {
		return registeredCity;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Address getAddress() { return address; }

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setRegisteredCity(String registeredCity) {
		this.registeredCity = registeredCity;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public void setLicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setAddress(Address address) {this.address = address; }

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		DriverRegistrationRequestDto other = (DriverRegistrationRequestDto)obj;
		return Objects.equal(firstname, other.firstname) &&
				Objects.equal(lastname, other.lastname) &&
				Objects.equal(birthday, other.birthday) &&
				Objects.equal(registeredCity, other.registeredCity) &&
				Objects.equal(vehicleName, other.vehicleName) &&
				Objects.equal(licensePlateNumber, other.licensePlateNumber) &&
				Objects.equal(phoneNumber, other.phoneNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(firstname, lastname, birthday, registeredCity, vehicleName, licensePlateNumber, phoneNumber);
	}

	@Override
	public String toString() {
		return "" + this.address.getLocality() + " " + this.address.getPinCode() +  " !!";
	}
}
