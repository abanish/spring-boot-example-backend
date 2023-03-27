package com.uber.driverdataregistry.domain.driver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.google.common.base.Objects;

/**
 * DriverProfileEntity is an entity that contains the personal data (driver profile) of a DriverAggregateRoot.
 */
@Embeddable
public class DriverProfileEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String firstname;

	private String lastname;

	private Date birthday;

	@OneToOne(cascade = CascadeType.ALL)
	private Address address;

	@OneToOne(cascade = CascadeType.ALL)
	private Vehicle currentVehicle;

	private String email;

	private String phoneNumber;

	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Vehicle> vehicleList;

	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Document> documentList;

	private boolean isVerified;

	public DriverProfileEntity() {
	}

	public DriverProfileEntity(String firstname, String lastname, Date birthday, Vehicle currentVehicle, String email, String phoneNumber, Address address) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthday = birthday;
		this.currentVehicle = currentVehicle;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.vehicleList = new ArrayList<>();
		this.vehicleList.add(currentVehicle);
		this.documentList = new ArrayList<>();
		this.address = address;
		this.isVerified = false;
	}

	public Date getBirthday() {
		return birthday;
	}

	public Vehicle getCurrentVehicle() {
		return currentVehicle;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public Collection<Vehicle> getVehicleList() {
		return vehicleList;
	}

	public Collection<Document> getDocumentList() {
		return documentList;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Address getAddress() { return address; }

	public boolean getIsVerified() { return isVerified; }

	public void setIsVerified(boolean verified) { this.isVerified = verified; }

	public void setAddress(Address a) {
		this.address = a;
	}

	public void moveToVehicle(Vehicle vehicle) {
		if (!vehicleList.contains(vehicle)) {
			vehicleList.add(vehicle);
		}
		if(!currentVehicle.equals(vehicle)) {
			setCurrentVehicle(vehicle);
		}
	}

	public void addDocument(Document doc) {
		documentList.add(doc);
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setCurrentVehicle(Vehicle currentVehicle) {
		this.currentVehicle = currentVehicle;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setVehicleList(Collection<Vehicle> vehicleList) {
		this.vehicleList = vehicleList;
	}
	public void setDocumentList(Collection<Document> documentList) {
		this.documentList = documentList;
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

		DriverProfileEntity other = (DriverProfileEntity)obj;
		return Objects.equal(firstname, other.firstname) &&
				Objects.equal(lastname, other.lastname) &&
				Objects.equal(birthday, other.birthday) &&
				Objects.equal(currentVehicle, other.currentVehicle) &&
				Objects.equal(email, other.email) &&
				Objects.equal(phoneNumber, other.phoneNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(firstname, lastname, birthday, currentVehicle, email, phoneNumber);
	}
}