package com.uber.driverselfservice.domain.driveronboarding;

import java.util.Date;

import com.uber.driverselfservice.interfaces.dtos.driveronboarding.DriverOnboardingRequestDto;

/**
 * DriverOnboardingRequestEvent is a domain event class that is used to notify the Driver Onboarding Management
 * when a new request has been submitted by a driver.
 * */
public class DriverOnboardingRequestEvent {
	private Date date;
	private DriverOnboardingRequestDto driverOnboardingRequestDto;

	public DriverOnboardingRequestEvent() {
	}

	public DriverOnboardingRequestEvent(Date date, DriverOnboardingRequestDto driverOnboardingRequestDto) {
		this.date = date;
		this.driverOnboardingRequestDto = driverOnboardingRequestDto;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public DriverOnboardingRequestDto getDriverOnboardingRequestDto() {
		return driverOnboardingRequestDto;
	}

	public void setDriverOnboardingRequestDto(DriverOnboardingRequestDto driverOnboardingRequestDto) {
		this.driverOnboardingRequestDto = driverOnboardingRequestDto;
	}
}
