package com.uber.driveronboardingmanagement.domain.driveronboarding;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DriverOnboardingEntity is an entity that represents a Driver Onboarding
 * which has been submitted as a response to a specific Driver Onboarding Request.
 */
@Entity
@Table(name = "driveronboardings")
public class DriverOnboardingEntity {
	@GeneratedValue
	@Id
	private Long id;

	private Date expirationDate;

	public DriverOnboardingEntity() {
	}

	public DriverOnboardingEntity(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}
}
