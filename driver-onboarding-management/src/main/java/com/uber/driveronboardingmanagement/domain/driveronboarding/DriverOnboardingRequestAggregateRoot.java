package com.uber.driveronboardingmanagement.domain.driveronboarding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * DriverOnboardingRequestAggregateRoot is the root entity of the Driver Onboarding Request aggregate. Note that there is
 * no class for the Driver Onboarding Request aggregate, so the package can be seen as aggregate.
 */
@Entity
@Table(name = "driveronboardingrequests")
public class DriverOnboardingRequestAggregateRoot {
	@Id
	private Long id;

	private Date date;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<RequestStatusChange> statusHistory;

	@OneToOne(cascade = CascadeType.ALL)
	private DriverInfoEntity driverInfo;

	@OneToOne(cascade = CascadeType.ALL)
	private DriverOnboardingEntity driverOnboarding;

	public DriverOnboardingRequestAggregateRoot() {}

	public DriverOnboardingRequestAggregateRoot(Long id, Date date, RequestStatus initialStatus, DriverInfoEntity driverInfo ) {
		this.id = id;
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
			throw new RuntimeException(String.format("Cannot change driver onboarding request status from %s to %s", getStatus(), newStatus));
		}
		statusHistory.add(new RequestStatusChange(date, newStatus));
	}

	public RequestStatusChange popStatus() {
		if(statusHistory.isEmpty()) {
			return null;
		}
		return statusHistory.remove(statusHistory.size()-1);
	}

	public void acceptRequest(Date date) {
		changeStatusTo(RequestStatus.ONBOARDING_REQUEST_SUCCESS, date);
	}

	public void rejectRequest(Date date) {
		changeStatusTo(RequestStatus.ONBOARDING_REQUEST_REJECTED, date);
	}

	public void acceptBackgroundVerification(Date date) {
		changeStatusTo(RequestStatus.BACKGROUND_VERIFICATION_SUCCESS, date);
	}

	public void rejectBackgroundVerification(Date date) {
		changeStatusTo(RequestStatus.BACKGROUND_VERIFICATION_FAILED, date);
	}

	public boolean checkOnboardingExpirationDate(Date date) {
		if(getStatus().canTransitionTo(RequestStatus.ONBOARDING_REQUEST_EXPIRED) && hasOnboardingExpired(date)) {
			markOnboardingAsExpired(date);
			return true;
		}
		return false;
	}

	public boolean hasOnboardingExpired(Date date) {
		return driverOnboarding != null && driverOnboarding.getExpirationDate().before(date);
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

	public DriverOnboardingEntity getDriverOnboarding() {
		return driverOnboarding;
	}

}