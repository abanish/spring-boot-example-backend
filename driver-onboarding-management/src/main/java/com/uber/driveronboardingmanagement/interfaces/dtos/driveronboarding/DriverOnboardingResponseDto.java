package com.uber.driveronboardingmanagement.interfaces.dtos.driveronboarding;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

/**
 * DriverOnboardingResponseDto is a data transfer object (DTO) that contains Uber's
 * response to a specific driver onboarding request.
 * */
public class DriverOnboardingResponseDto {
	@Valid
	@NotNull
	private String status;

	@Valid
	@Future
	private Date expirationDate;

	public DriverOnboardingResponseDto() {
	}

	public DriverOnboardingResponseDto(String status, Date expirationDat) {
		this.status = status;
		this.expirationDate = expirationDate;
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