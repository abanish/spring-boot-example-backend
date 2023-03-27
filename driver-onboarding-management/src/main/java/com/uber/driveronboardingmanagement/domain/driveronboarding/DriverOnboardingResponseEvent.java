package com.uber.driveronboardingmanagement.domain.driveronboarding;

import java.util.Date;

/**
 * DriverOnboardingResponseEvent is a domain event class that is used to notify the Driver Self-Service Backend
 * when Uber has submitted a response for a specific Driver Onboarding Request.
 * */
public class DriverOnboardingResponseEvent  {
	private Date date;
	private Long driverOnboardingRequestId;
	private boolean requestAccepted;
	private Date expirationDate;

	public DriverOnboardingResponseEvent() {
	}

	public DriverOnboardingResponseEvent(Date date, Long driverOnboardingRequestId, boolean requestAccepted, Date expirationDate) {
		this.date = date;
		this.driverOnboardingRequestId = driverOnboardingRequestId;
		this.requestAccepted = requestAccepted;
		this.expirationDate = expirationDate;
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

	public boolean isRequestAccepted() {
		return requestAccepted;
	}

	public void setRequestAccepted(boolean requestAccepted) {
		this.requestAccepted = requestAccepted;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
}