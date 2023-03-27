package com.uber.driveronboardingmanagement.domain.driveronboarding;

import java.util.Date;

/**
 * DriverOnboardingExpiredEvent is a domain event class that is used to notify the Driver Self-Service Backend
 * when the Driver Onboarding for a specific Driver Onboarding Request has expired.
 * */
public class DriverOnboardingExpiredEvent {
	private Date date;
	private Long driverOnboardingRequestId;

	public DriverOnboardingExpiredEvent() {
	}

	public DriverOnboardingExpiredEvent(Date date, Long driverOnboardingRequestId) {
		this.date = date;
		this.driverOnboardingRequestId = driverOnboardingRequestId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getDriverOnboardingRequestId() {
		return driverOnboardingRequestId;
	}

	public void setDriverOnboardingRequestId(Long driverOnboardingRequestId) {
		this.driverOnboardingRequestId = driverOnboardingRequestId;
	}
}