package com.uber.driverdataregistry.interfaces.dtos.driver;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Objects;
import com.uber.driverdataregistry.domain.driver.Address;
import com.uber.driverdataregistry.domain.driver.Vehicle;
import com.uber.driverdataregistry.domain.driver.DriverProfileEntity;
import com.uber.driverdataregistry.interfaces.validation.PhoneNumber;

/**
 * DriverProfileUpdateRequestDto is a data transfer object (DTO) that represents the personal data (driver profile) of a driver.
 * It is sent to the DriverController when a new driver is created or the profile of an existing driver is updated.
 */
public class DriverProfileUpdateRequestDto {
	@NotEmpty
	private String firstname;

	@NotEmpty
	private String lastname;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date birthday;

	@NotEmpty
	private String vehicleName;

	@NotEmpty
	private String licensePlateNumber;

	@NotEmpty
	private String registeredCity;

	@Email
	@NotEmpty
	private String email;

	@PhoneNumber
	private String phoneNumber;

	@NotEmpty
	private String locality;

	@NotEmpty
	private String pinCode;

	@NotEmpty
	private String city;

	public DriverProfileUpdateRequestDto() {
	}

	public DriverProfileUpdateRequestDto(String firstname, String lastname, Date birthday, String vehicleName, String licensePlateNumber, String registeredCity, String email, String phoneNumber, String locality, String pinCode, String city) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthday = birthday;
		this.vehicleName = vehicleName;
		this.licensePlateNumber = licensePlateNumber;
		this.registeredCity = registeredCity;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.locality = locality;
		this.pinCode = pinCode;
		this.city = city;
	}

	public DriverProfileEntity toDomainObject() {
		Vehicle vehicle = new Vehicle(getVehicleName(), getlicensePlateNumber(), getRegisteredCity());
		Address address = new Address(getLocality(), getPinCode(), getCity());
		return new DriverProfileEntity(getFirstname(), getLastname(), getBirthday(), vehicle, getEmail(), getPhoneNumber(), address);
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

	public Date getBirthday() {
		return birthday;
	}

	public String getLocality() { return this.locality; }

	public String getPinCode() { return this.pinCode; }

	public String getCity() { return this.city; }

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public String getlicensePlateNumber() {
		return licensePlateNumber;
	}

	public void setlicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}

	public String getRegisteredCity() {
		return registeredCity;
	}

	public void setRegisteredCity(String registeredCity) {
		this.registeredCity = registeredCity;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


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

		DriverProfileUpdateRequestDto other = (DriverProfileUpdateRequestDto)obj;
		return Objects.equal(firstname, other.firstname) &&
				Objects.equal(lastname, other.lastname) &&
				Objects.equal(birthday, other.birthday) &&
				Objects.equal(registeredCity, other.registeredCity) &&
				Objects.equal(vehicleName, other.vehicleName) &&
				Objects.equal(licensePlateNumber, other.licensePlateNumber) &&
				Objects.equal(email, other.email) &&
				Objects.equal(phoneNumber, other.phoneNumber) &&
				Objects.equal(pinCode, other.pinCode) &&
				Objects.equal(city, other.city) &&
				Objects.equal(locality, other.locality);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(firstname, lastname, birthday, registeredCity, vehicleName, licensePlateNumber, email, phoneNumber);
	}
}
