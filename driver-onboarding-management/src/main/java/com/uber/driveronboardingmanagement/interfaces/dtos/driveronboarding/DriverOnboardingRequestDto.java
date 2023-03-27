package com.uber.driveronboardingmanagement.interfaces.dtos.driveronboarding;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.uber.driveronboardingmanagement.domain.driveronboarding.DriverOnboardingRequestAggregateRoot;

/**
 * DriverOnboardingRequestDto is a data transfer object (DTO) that represents a request
 * by a driver for a new driver onboarding.
 */
public class DriverOnboardingRequestDto {
	private Long id;

	@Valid
	private Date date;

	@Valid
	private List<RequestStatusChangeDto> statusHistory;

	@Valid
	@NotNull
	private DriverInfoDto driverInfo;

	public DriverOnboardingRequestDto() {
	}

	public DriverOnboardingRequestDto(Long id, Date date, List<RequestStatusChangeDto> statusHistory, DriverInfoDto driverInfo) {
		this.id = id;
		this.date = date;
		this.statusHistory = statusHistory;
		this.driverInfo = driverInfo;
	}

	public static DriverOnboardingRequestDto fromDomainObject(DriverOnboardingRequestAggregateRoot driverOnboardingRequest) {
		Long id = driverOnboardingRequest.getId();
		Date date = driverOnboardingRequest.getDate();
		List<RequestStatusChangeDto> statusHistory = driverOnboardingRequest.getStatusHistory().stream()
				.map(RequestStatusChangeDto::fromDomainObject)
				.collect(Collectors.toList());
		DriverInfoDto driverInfoDto = DriverInfoDto.fromDomainObject(driverOnboardingRequest.getDriverInfo());
		DriverOnboardingDto driverOnboardingDto = driverOnboardingRequest.getDriverOnboarding() != null ? DriverOnboardingDto.fromDomainObject(driverOnboardingRequest.getDriverOnboarding()) : null;
		return new DriverOnboardingRequestDto(id, date, statusHistory, driverInfoDto);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<RequestStatusChangeDto> getStatusHistory() {
		return statusHistory;
	}

	public void setStatusHistory(List<RequestStatusChangeDto> statusHistory) {
		this.statusHistory = statusHistory;
	}

	public DriverInfoDto getDriverInfo() {
		return driverInfo;
	}

	public void setDriverInfo(DriverInfoDto driverInfo) {
		this.driverInfo = driverInfo;
	}

}