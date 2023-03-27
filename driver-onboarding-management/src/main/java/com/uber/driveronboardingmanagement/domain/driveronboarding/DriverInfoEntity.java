package com.uber.driveronboardingmanagement.domain.driveronboarding;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.uber.driveronboardingmanagement.domain.driver.DriverId;

/**
 * DriverInfoEntity is an entity that is part of an DriverOnboardingRequestAggregateRoot
 * and contains infos about the initiator of the request.
 */
@Entity
@Table(name = "driverinfos")
public class DriverInfoEntity {
	@GeneratedValue
	@Id
	private Long id;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="id", column=@Column(name="driverId"))})
	private final DriverId driverId;

	private final String firstname;

	private final String lastname;

	@OneToOne(cascade = CascadeType.ALL)
	private final Vehicle currentVehicle;

	public DriverInfoEntity() {
		this.driverId = null;
		this.firstname = null;
		this.lastname = null;
		this.currentVehicle = null;
	}

	public DriverInfoEntity(DriverId driverId, String firstname, String lastname, Vehicle currentVehicle) {
		this.driverId = driverId;
		this.firstname = firstname;
		this.lastname = lastname;
		this.currentVehicle = currentVehicle;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DriverId getDriverId() {
		return driverId;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public Vehicle getCurrentVehicle() {
		return currentVehicle;
	}

}

