package com.uber.driverdataregistry.domain.driver;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * DriverAggregateRoot is the root entity of the Driver aggregate.
 */
@Entity
@Table(name = "drivers")
public class DriverAggregateRoot {

	@EmbeddedId
	private DriverId id;

	@Embedded
	private DriverProfileEntity driverProfile;

	public DriverAggregateRoot() {
	}

	public DriverAggregateRoot(DriverId id, DriverProfileEntity driverProfile) {
		this.id = id;
		this.driverProfile = driverProfile;
	}

	public DriverProfileEntity getDriverProfile() {
		return driverProfile;
	}

	public DriverId getId() {
		return id;
	}

	public void moveToVehicle(Vehicle vehicle) {
		driverProfile.moveToVehicle(vehicle);
	}

	public void addDocument(Document doc) {
		driverProfile.addDocument(doc);
	}

	public void updateDriverProfile(DriverProfileEntity updatedDriverProfile) {
		driverProfile.setFirstname(updatedDriverProfile.getFirstname());
		driverProfile.setLastname(updatedDriverProfile.getLastname());
		driverProfile.setBirthday(updatedDriverProfile.getBirthday());
		driverProfile.moveToVehicle(updatedDriverProfile.getCurrentVehicle());
		driverProfile.setEmail(updatedDriverProfile.getEmail());
		driverProfile.setPhoneNumber(DriverFactory.formatPhoneNumber(updatedDriverProfile.getPhoneNumber()));
		driverProfile.setAddress(new Address(updatedDriverProfile.getAddress().getLocality(), updatedDriverProfile.getAddress().getPinCode(),
				updatedDriverProfile.getAddress().getCity()));
	}
}
