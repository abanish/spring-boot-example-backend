package com.uber.driveronboardingmanagement.interfaces.dtos.driveronboarding;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.uber.driveronboardingmanagement.domain.driveronboarding.DriverOnboardingEntity;
/**
 * DriverOnboardingDto is a data transfer object (DTO) that represents an Driver Onboarding
 * which has been submitted as a response to a specific Driver Onboarding Request.
 */
public class DriverOnboardingDto {
	@Valid
	@NotNull
	private Date expirationDate;

	public DriverOnboardingDto() {
	}

	private DriverOnboardingDto(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public static DriverOnboardingDto fromDomainObject(DriverOnboardingEntity driverOnboarding) {
		Date expirationDate = driverOnboarding.getExpirationDate();
		return new DriverOnboardingDto(expirationDate);
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

}