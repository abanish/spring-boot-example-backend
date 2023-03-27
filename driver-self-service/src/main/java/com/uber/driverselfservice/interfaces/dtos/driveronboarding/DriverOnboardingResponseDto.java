package com.uber.driverselfservice.interfaces.dtos.driveronboarding;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * DriverOnboardingResponseDto is a data transfer object (DTO) that contains Uber's
 * response to a specific driver onboarding request.
 * */
public class DriverOnboardingResponseDto {
	@NotEmpty
	private String status;

	@Valid
	private Date expirationDate;

	public DriverOnboardingResponseDto() {
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
}