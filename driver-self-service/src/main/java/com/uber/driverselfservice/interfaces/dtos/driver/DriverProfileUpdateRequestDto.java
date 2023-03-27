package com.uber.driverselfservice.interfaces.dtos.driver;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Objects;
import com.uber.driverselfservice.domain.driver.Address;
import com.uber.driverselfservice.interfaces.validation.PhoneNumber;

/**
 * DriverProfileUpdateRequestDto is a data transfer object (DTO) that represents the personal data (driver profile) of a driver.
 * It is sent to the Driver Data Registry when a new driver is created.
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

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}

	public void setLicensePlateNumber(String licensePlateNumber) {
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

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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
				Objects.equal(phoneNumber, other.phoneNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(firstname, lastname, birthday, registeredCity, vehicleName, licensePlateNumber, email, phoneNumber);
	}

	@Override
	public String toString() {
		return this.locality;
	}
}
