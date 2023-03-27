package com.uber.driverdataregistry.interfaces.dtos.driver;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import com.uber.driverdataregistry.domain.driver.Address;
import com.uber.driverdataregistry.domain.driver.Vehicle;
import org.springframework.hateoas.RepresentationModel;

import com.uber.driverdataregistry.domain.driver.DriverAggregateRoot;
import com.uber.driverdataregistry.domain.driver.DriverProfileEntity;

/**
 * The DriverResponseDto represents a driver, including their complete vehicle list.
 */
public class DriverResponseDto extends RepresentationModel {
	private final String driverId;

	private final String firstname;

	private final String lastname;

	private final Date birthday;

	private final String vehicleName;

	private final String licensePlateNumber;

	private final String registeredCity;

	private final String email;

	private final String phoneNumber;

	private final Collection<Vehicle> vehicleList;

	private final Address address;

	private boolean isVerified;

	public DriverResponseDto(Set<String> includedFields, DriverAggregateRoot driver) {
		this.driverId = select(includedFields, "driverId", driver.getId().getId());

		final DriverProfileEntity profile = driver.getDriverProfile();
		this.firstname = select(includedFields, "firstname", profile.getFirstname());
		this.lastname = select(includedFields, "lastname", profile.getLastname());
		this.birthday = select(includedFields, "birthday", profile.getBirthday());
		this.vehicleName = select(includedFields, "vehicleName", profile.getCurrentVehicle().getVehicleName());
		this.licensePlateNumber = select(includedFields, "licensePlateNumber", profile.getCurrentVehicle().getLicensePlateNumber());
		this.registeredCity = select(includedFields, "registeredCity", profile.getCurrentVehicle().getRegisteredCity());
		this.email = select(includedFields, "email", profile.getEmail());
		this.phoneNumber = select(includedFields, "phoneNumber", profile.getPhoneNumber());
		this.vehicleList = select(includedFields, "vehicleList", profile.getVehicleList());
		this.address = select(includedFields, "address", new Address(profile.getAddress().getLocality(), profile.getAddress().getPinCode(), profile.getAddress().getCity()));
		this.isVerified = select(includedFields, "isVerified", profile.getIsVerified());
	}

	private static <T> T select(Set<String> includedFields, String fieldName, T value) {
		if(includedFields.isEmpty() || includedFields.contains(fieldName)) {
			return value;
		} else {
			return null;
		}
	}

	public String getDriverId() {
		return driverId;
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

	public String getVehicleName() {
		return vehicleName;
	}

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}

	public String getRegisteredCity() {
		return registeredCity;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Collection<Vehicle> getVehicleList() {
		return vehicleList;
	}

	public Address getAddress() { return this.address; }

	public boolean getIsVerified() { return this.isVerified; }
}