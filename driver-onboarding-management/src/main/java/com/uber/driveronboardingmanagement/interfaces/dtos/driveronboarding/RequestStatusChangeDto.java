package com.uber.driveronboardingmanagement.interfaces.dtos.driveronboarding;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import com.uber.driveronboardingmanagement.domain.driveronboarding.RequestStatusChange;

/**
 * RequestStatusChangeDto is a data transfer object (DTO) that represents a status change of an driver onboarding request.
 */
public class RequestStatusChangeDto {
	@Valid
	private Date date;

	@NotEmpty
	private String status;

	public RequestStatusChangeDto() {
	}

	public RequestStatusChangeDto(Date date, String status) {
		this.date = date;
		this.status = status;
	}

	public static RequestStatusChangeDto fromDomainObject(RequestStatusChange requestStatusChange) {
		return new RequestStatusChangeDto(requestStatusChange.getDate(), requestStatusChange.getStatus().name());
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}