package com.uber.driveronboardingmanagement.domain.driveronboarding;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A Vehicle is a value object that is used to represent the vehicle of a driver.
 * */
@Entity
@Table(name = "vehicles")
public class Vehicle {
	@GeneratedValue
	@Id
	private Long id;

	private final String vehicleName;

	private final String licensePlateNumber;

	private final String registeredCity;

	public Vehicle() {
		this.vehicleName = null;
		this.licensePlateNumber = null;
		this.registeredCity = null;
	}

	public Vehicle(String vehicleName, String licensePlateNumber, String registeredCity) {
		this.vehicleName = vehicleName;
		this.licensePlateNumber = licensePlateNumber;
		this.registeredCity = registeredCity;
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

	@Override
	public String toString() {
		return String.format("%s, %s %ss", vehicleName, licensePlateNumber, registeredCity);
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

		Vehicle other = (Vehicle) obj;
		return Objects.equals(vehicleName, other.vehicleName) &&
				Objects.equals(licensePlateNumber, other.licensePlateNumber) &&
				Objects.equals(registeredCity, other.registeredCity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(vehicleName, licensePlateNumber, registeredCity);
	}
}