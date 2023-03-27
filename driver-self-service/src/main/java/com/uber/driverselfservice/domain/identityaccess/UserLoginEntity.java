package com.uber.driverselfservice.domain.identityaccess;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.uber.driverselfservice.domain.driver.DriverId;

/**
 * UserLogin is an entity that contains the login credentials of a specific user.
 */
@Entity
@Table(name = "user_logins")
public class UserLoginEntity {

	@Id
	@GeneratedValue
	private Long id;
	private String authorities;
	private String email;
	private String password;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="id", column=@Column(name="driverId"))})
	private DriverId driverId;

	public UserLoginEntity() {
	}

	public UserLoginEntity(String email, String password, String authorities, DriverId driverId) {
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		this.driverId = driverId;
	}

	public String getAuthorities() {
		return authorities;
	}

	public DriverId getDriverId() {
		return driverId;
	}

	public String getEmail() {
		return email;
	}

	public Long getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public void setDriverId(DriverId driverId) {
		this.driverId = driverId;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
