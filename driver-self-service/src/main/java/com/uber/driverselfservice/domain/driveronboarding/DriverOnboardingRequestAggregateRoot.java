package com.uber.driverselfservice.domain.driveronboarding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * DriverOnboardingRequestAggregateRoot is the root entity of the driver onboarding Request aggregate.
 */
@Entity
@Table(name = "driveronboardingrequests")
public class DriverOnboardingRequestAggregateRoot {
	@Id
	@GeneratedValue
	private Long id;

	private Date date;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<RequestStatusChange> statusHistory;

	@OneToOne(cascade = CascadeType.ALL)
	private DriverInfoEntity driverInfo;

	public DriverOnboardingRequestAggregateRoot() {}

	public DriverOnboardingRequestAggregateRoot(Date date, RequestStatus initialStatus, DriverInfoEntity driverInfo) {
		this.date = date;
		List<RequestStatusChange> statusHistory = new ArrayList<>();
		statusHistory.add(new RequestStatusChange(date, initialStatus));
		this.statusHistory = statusHistory;
		this.driverInfo = driverInfo;
	}

	public Long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public RequestStatus getStatus() {
		return statusHistory.get(statusHistory.size()-1).getStatus();
	}

	public List<RequestStatusChange> getStatusHistory() {
		return statusHistory;
	}

	private void changeStatusTo(RequestStatus newStatus, Date date) {
		if (!getStatus().canTransitionTo(newStatus)) {
			throw new RuntimeException(String.format("Cannot change onboarding request status from %s to %s", getStatus(), newStatus));
		}
		statusHistory.add(new RequestStatusChange(date, newStatus));
	}

	public void markOnboardingAsExpired(Date date) {
		changeStatusTo(RequestStatus.ONBOARDING_REQUEST_EXPIRED, date);
	}

	public void finalizeOnboarding(Date date) {
		changeStatusTo(RequestStatus.ONBOARDING_REQUEST_SUCCESS, date);
	}

	public DriverInfoEntity getDriverInfo() {
		return driverInfo;
	}
}
